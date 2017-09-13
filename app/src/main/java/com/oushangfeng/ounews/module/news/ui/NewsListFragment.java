package com.oushangfeng.ounews.module.news.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.base.BaseFragment;
import com.oushangfeng.ounews.base.BaseRecyclerAdapter;
import com.oushangfeng.ounews.base.BaseRecyclerViewHolder;
import com.oushangfeng.ounews.base.BaseSpacesItemDecoration;
import com.oushangfeng.ounews.bean.TsinghuaNewsSummary;
import com.oushangfeng.ounews.module.news.ui.adapter.NewsListFragmentAdapter;
import com.oushangfeng.ounews.bean.NeteastNewsSummary;
import com.oushangfeng.ounews.bean.SinaPhotoDetail;
import com.oushangfeng.ounews.callback.OnEmptyClickListener;
import com.oushangfeng.ounews.callback.OnItemClickAdapter;
import com.oushangfeng.ounews.callback.OnLoadMoreListener;
import com.oushangfeng.ounews.common.DataLoadType;
import com.oushangfeng.ounews.module.news.presenter.INewsListPresenter;
import com.oushangfeng.ounews.module.news.presenter.INewsListPresenterImpl;
import com.oushangfeng.ounews.module.news.view.INewsListView;
import com.oushangfeng.ounews.module.photo.ui.PhotoDetailActivity;
import com.oushangfeng.ounews.utils.ClickUtils;
import com.oushangfeng.ounews.utils.GlideUtils;
import com.oushangfeng.ounews.utils.MeasureUtil;
import com.oushangfeng.ounews.widget.ThreePointLoadingView;
import com.oushangfeng.ounews.widget.refresh.RefreshLayout;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: NewsListFragment<p>
 * Author: oubowu<p>
 * Fuction: 新闻列表界面<p>
 * CreateDate: 2016/2/17 20:50<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.fragment_news_list,
        handleRefreshLayout = true)
public class NewsListFragment extends BaseFragment<INewsListPresenter> implements INewsListView {

    protected static final String NEWS_ID = "news_id";
    protected static final String NEWS_TYPE = "news_type";
    protected static final String POSITION = "position";

    protected String mNewsId;
    protected String mNewsType;

    protected int mCategory;
    protected int mPageSize;

    private NewsListFragmentAdapter<TsinghuaNewsSummary> mAdapter;
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;

    private SinaPhotoDetail mSinaPhotoDetail;

    private ThreePointLoadingView mLoadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getInt("category");
            mPageSize = getArguments().getInt("pageSize");
            mPosition = getArguments().getInt(POSITION);
        }

    }

    public static NewsListFragment newInstance(int category, int position) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("category", category);
        bundle.putInt("pageSize", 20);
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View fragmentRootView) {

        mLoadingView = (ThreePointLoadingView) fragmentRootView.findViewById(R.id.tpl_view);
        mLoadingView.setOnClickListener(this);

        mRecyclerView = (RecyclerView) fragmentRootView.findViewById(R.id.recycler_view);

        mRefreshLayout = (RefreshLayout) fragmentRootView.findViewById(R.id.refresh_layout);

        mPresenter = new INewsListPresenterImpl(this, mPageSize, mCategory);

    }

    @Override
    public void showProgress() {
        mLoadingView.play();
    }

    @Override
    public void hideProgress() {
        mLoadingView.stop();
    }

    @Override
    public void updateNewsList(final List<TsinghuaNewsSummary> data, String errorMsg, @DataLoadType.DataLoadTypeChecker int type) {

        if (mAdapter == null) {
            initNewsList(data);
        }

        mAdapter.showEmptyView(false, "");

        switch (type) {
            case DataLoadType.TYPE_REFRESH_SUCCESS:
                mRefreshLayout.refreshFinish();
                mAdapter.enableLoadMore(true);
                mAdapter.setData(data);
                break;
            case DataLoadType.TYPE_REFRESH_FAIL:
                mRefreshLayout.refreshFinish();
                mAdapter.enableLoadMore(false);
                mAdapter.showEmptyView(true, errorMsg);
                mAdapter.notifyDataSetChanged();
                break;
            case DataLoadType.TYPE_LOAD_MORE_SUCCESS:
                mAdapter.loadMoreSuccess();
                if (data == null || data.size() == 0) {
                    mAdapter.enableLoadMore(null);
                    toast("全部加载完毕");
                    return;
                }
                mAdapter.addMoreData(data);
                break;
            case DataLoadType.TYPE_LOAD_MORE_FAIL:
                mAdapter.loadMoreFailed(errorMsg);
                break;
        }
    }

    private void initNewsList(final List<TsinghuaNewsSummary> data) {
        // mAdapter为空肯定为第一次进入状态
        mAdapter = new NewsListFragmentAdapter<TsinghuaNewsSummary>(getActivity(), data) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_news_summary;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, int position, TsinghuaNewsSummary item) {
                String pic[] = item.pictures.split(";| ");
                if (pic.length == 0)
                {
                    pic = new String[1];
                    pic[0] = "";
                }
                GlideUtils.loadDefault(pic[0], holder.getImageView(R.id.iv_news_summary_photo), null, null, DiskCacheStrategy.RESULT);
                //                Glide.with(getActivity()).load(item.imgsrc).asBitmap().animate(R.anim.image_load).diskCacheStrategy(DiskCacheStrategy.RESULT)
                //                        .placeholder(R.drawable.ic_loading).error(R.drawable.ic_fail).into(holder.getImageView(R.id.iv_news_summary_photo));
                //add by lcy
                if(item.hasread == true){
                    holder.getTextView(R.id.tv_news_summary_title).setTextColor(Color.GRAY);
                    holder.getTextView(R.id.tv_news_summary_digest).setTextColor(Color.GRAY);
                    holder.getTextView(R.id.tv_news_summary_ptime).setTextColor(Color.GRAY);
                }
                else{
                    holder.getTextView(R.id.tv_news_summary_title).setTextColor(Color.BLACK);
                    holder.getTextView(R.id.tv_news_summary_digest).setTextColor(Color.BLACK);
                    holder.getTextView(R.id.tv_news_summary_ptime).setTextColor(Color.BLACK);
                }
                holder.getTextView(R.id.tv_news_summary_title).setText(item.title);
                holder.getTextView(R.id.tv_news_summary_digest).setText(item.intro);
                holder.getTextView(R.id.tv_news_summary_ptime).setText(item.time);
            }
        };

        mAdapter.setOnItemClickListener(new OnItemClickAdapter() {
            @Override
            public void onItemClick(View view, int position) {

                if (ClickUtils.isFastDoubleClick()) {
                    return;
                }

                // imgextra不为空的话，无新闻内容，直接打开图片浏览
                KLog.e(mAdapter.getData().get(position).title + ";" + mAdapter.getData().get(position).id);

                view = view.findViewById(R.id.iv_news_summary_photo);

                if (mAdapter.getData().get(position).id == null) {
                    toast("此新闻浏览不了哎╮(╯Д╰)╭");
                    return;
                }
                //add by lcy: for gray flag
                mAdapter.getData().get(position).hasread = true;


                // 跳转到新闻详情
                if (!TextUtils.isEmpty(mAdapter.getData().get(position).intro)) {
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("postid", mAdapter.getData().get(position).id);
                    intent.putExtra("imgsrc", mAdapter.getData().get(position).pictures);


                    ObjectMapper mapper = new ObjectMapper();
                    // Convert object to JSON string
                    String json = "";
                    try {
                        json = mapper.writeValueAsString(mAdapter.getData().get(position));
                    }catch (Exception e){
                    }
                    intent.putExtra("summary",json);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view.findViewById(R.id.iv_news_summary_photo), "photos");
                        getActivity().startActivity(intent, options.toBundle());
                    } else {
                        //让新的Activity从一个小的范围扩大到全屏
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth()/* / 2*/, view.getHeight()/* / 2*/, 0, 0);
                        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                    }
                } else {
                    // 以下将数据封装成新浪需要的格式，用于点击跳转到图片浏览
                    mSinaPhotoDetail = new SinaPhotoDetail();
                    mSinaPhotoDetail.data = new SinaPhotoDetail.SinaPhotoDetailDataEntity();
                    mSinaPhotoDetail.data.title = mAdapter.getData().get(position).title;
                    mSinaPhotoDetail.data.content = "";
                    mSinaPhotoDetail.data.pics = new ArrayList<>();
                    // 天啊，什么格式都有 --__--
                    /*if (mAdapter.getData().get(position).ads != null) {
                        for (NeteastNewsSummary.AdsEntity entiity : mAdapter.getData().get(position).ads) {
                            SinaPhotoDetail.SinaPhotoDetailPicsEntity sinaPicsEntity = new SinaPhotoDetail.SinaPhotoDetailPicsEntity();
                            sinaPicsEntity.pic = entiity.imgsrc;
                            sinaPicsEntity.alt = entiity.title;
                            sinaPicsEntity.kpic = entiity.imgsrc;
                            mSinaPhotoDetail.data.pics.add(sinaPicsEntity);
                        }
                    } else if (mAdapter.getData().get(position).imgextra != null) {
                        for (NeteastNewsSummary.ImgextraEntity entiity : mAdapter.getData().get(position).imgextra) {
                            SinaPhotoDetail.SinaPhotoDetailPicsEntity sinaPicsEntity = new SinaPhotoDetail.SinaPhotoDetailPicsEntity();
                            sinaPicsEntity.pic = entiity.imgsrc;
                            sinaPicsEntity.kpic = entiity.imgsrc;
                            mSinaPhotoDetail.data.pics.add(sinaPicsEntity);
                        }
                    }*/

                    Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
                    intent.putExtra("neteast", mSinaPhotoDetail);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
                    ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

                }
            }
        });

        mAdapter.setOnEmptyClickListener(new OnEmptyClickListener() {
            @Override
            public void onEmptyClick() {
                showProgress();
                mPresenter.refreshData();
            }
        });

        mAdapter.setOnLoadMoreListener(10, new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                mPresenter.loadMoreData();
                // mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }
        });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new BaseSpacesItemDecoration(MeasureUtil.dip2px(getActivity(), 4)));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.getItemAnimator().setAddDuration(250);
        mRecyclerView.getItemAnimator().setMoveDuration(250);
        mRecyclerView.getItemAnimator().setChangeDuration(250);
        mRecyclerView.getItemAnimator().setRemoveDuration(250);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                mPresenter.refreshData();
            }
        });
    }
}
