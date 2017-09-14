package com.java.two.module.news.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
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
import com.java.two.R;
import com.java.two.annotation.ActivityFragmentInject;
import com.java.two.app.AppManager;
import com.java.two.base.BaseActivity;
import com.java.two.base.BasePresenter;
import com.java.two.base.BaseRecyclerAdapter;
import com.java.two.base.BaseRecyclerViewHolder;
import com.java.two.base.BaseSpacesItemDecoration;
import com.java.two.bean.SinaPhotoDetail;
import com.java.two.bean.TsinghuaNewsSummary;
import com.java.two.callback.OnEmptyClickListener;
import com.java.two.callback.OnItemClickAdapter;
import com.java.two.callback.OnLoadMoreListener;
import com.java.two.common.DataLoadType;
import com.java.two.module.news.presenter.INewsSearchingPresenter;
import com.java.two.module.news.presenter.INewsSearchingPresenterImpl;
import com.java.two.module.news.view.INewsSearchingView;
import com.java.two.module.photo.ui.PhotoDetailActivity;
import com.java.two.utils.ClickUtils;
import com.java.two.utils.GlideUtils;
import com.java.two.utils.MeasureUtil;
import com.java.two.utils.ViewUtil;
import com.java.two.widget.ThreePointLoadingView;
import com.java.two.widget.refresh.RefreshLayout;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

@ActivityFragmentInject(contentViewId = R.layout.activity_news_searching,
        menuId = R.menu.menu_settings,
        toolbarTitle = R.string.news_search,
        enableSlidr = true)
public class NewsSearchingActivity extends BaseActivity<BasePresenter> implements INewsSearchingView{
    //private mAdapter;

    private ThreePointLoadingView mLoadingView;
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private INewsSearchingPresenter mPresenter;

    private BaseRecyclerAdapter<TsinghuaNewsSummary> mAdapter;

    private SinaPhotoDetail mSinaPhotoDetail;

    private String mKeywords;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mKeywords = getIntent().getStringExtra("Keywords");
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {

        // 设了默认的windowBackground使得冷启动没那么突兀，这里再设置为空减少过度绘制
        getWindow().setBackgroundDrawable(null);
        ViewUtil.quitFullScreen(this);

        AppManager.getAppManager().orderNavActivity(getClass().getName(), false);

        mLoadingView = (ThreePointLoadingView) findViewById(R.id.tpl_view3);
        mLoadingView.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view3);

        mRefreshLayout = (RefreshLayout) findViewById(R.id.searching_layout);

        mPresenter = new INewsSearchingPresenterImpl(this, mKeywords);

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

    private void initNewsList(final List<TsinghuaNewsSummary> data){
        // mAdapter为空肯定为第一次进入状态
        mAdapter = new BaseRecyclerAdapter<TsinghuaNewsSummary>(this, data) {
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


                // 跳转到新闻详情
                if (!TextUtils.isEmpty(mAdapter.getData().get(position).intro)) {
                    Intent intent = new Intent((Activity)mAdapter.getmContext(), NewsDetailActivity.class);
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
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity)mAdapter.getmContext(), view.findViewById(R.id.iv_news_summary_photo), "photos");
                        mAdapter.getmContext().startActivity(intent, options.toBundle());
                    } else {
                        //让新的Activity从一个小的范围扩大到全屏
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth()/* / 2*/, view.getHeight()/* / 2*/, 0, 0);
                        ActivityCompat.startActivity((Activity)mAdapter.getmContext(), intent, options.toBundle());
                    }
                } else {
                    // 以下将数据封装成新浪需要的格式，用于点击跳转到图片浏览
                    mSinaPhotoDetail = new SinaPhotoDetail();
                    mSinaPhotoDetail.data = new SinaPhotoDetail.SinaPhotoDetailDataEntity();
                    mSinaPhotoDetail.data.title = mAdapter.getData().get(position).title;
                    mSinaPhotoDetail.data.content = "";
                    mSinaPhotoDetail.data.pics = new ArrayList<>();
                    /*
                    // 天啊，什么格式都有 --__--
                    if (mAdapter.getData().get(position).ads != null) {
                        for (TsinghuaNewsSummary.AdsEntity entiity : mAdapter.getData().get(position).ads) {
                            SinaPhotoDetail.SinaPhotoDetailPicsEntity sinaPicsEntity = new SinaPhotoDetail.SinaPhotoDetailPicsEntity();
                            sinaPicsEntity.pic = entiity.imgsrc;
                            sinaPicsEntity.alt = entiity.title;
                            sinaPicsEntity.kpic = entiity.imgsrc;
                            mSinaPhotoDetail.data.pics.add(sinaPicsEntity);
                        }
                    } else if (mAdapter.getData().get(position).imgextra != null) {
                        for (TsinghuaNewsSummary.ImgextraEntity entiity : mAdapter.getData().get(position).imgextra) {
                            SinaPhotoDetail.SinaPhotoDetailPicsEntity sinaPicsEntity = new SinaPhotoDetail.SinaPhotoDetailPicsEntity();
                            sinaPicsEntity.pic = entiity.imgsrc;
                            sinaPicsEntity.kpic = entiity.imgsrc;
                            mSinaPhotoDetail.data.pics.add(sinaPicsEntity);
                        }
                    }
                    */
                    Intent intent = new Intent((Activity)mAdapter.getmContext(), PhotoDetailActivity.class);
                    intent.putExtra("neteast", mSinaPhotoDetail);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
                    ActivityCompat.startActivity((Activity)mAdapter.getmContext(), intent, options.toBundle());

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

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Activity)mAdapter.getmContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new BaseSpacesItemDecoration(MeasureUtil.dip2px((Activity)mAdapter.getmContext(), 4)));
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
