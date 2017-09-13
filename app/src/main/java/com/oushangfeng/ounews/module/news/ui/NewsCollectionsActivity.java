package com.oushangfeng.ounews.module.news.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.app.AppManager;
import com.oushangfeng.ounews.base.BaseActivity;
import com.oushangfeng.ounews.base.BasePresenter;
import com.oushangfeng.ounews.base.BaseRecyclerAdapter;
import com.oushangfeng.ounews.base.BaseRecyclerViewHolder;
import com.oushangfeng.ounews.base.BaseSpacesItemDecoration;
import com.oushangfeng.ounews.bean.NeteastNewsSummary;
import com.oushangfeng.ounews.bean.SinaPhotoDetail;
import com.oushangfeng.ounews.bean.TsinghuaNewsSummary;
import com.oushangfeng.ounews.callback.OnEmptyClickListener;
import com.oushangfeng.ounews.callback.OnItemClickAdapter;
import com.oushangfeng.ounews.callback.OnLoadMoreListener;
import com.oushangfeng.ounews.callback.SimpleItemTouchHelperCallback;
import com.oushangfeng.ounews.common.DataLoadType;
import com.oushangfeng.ounews.greendao.NewsChannelTable;
import com.oushangfeng.ounews.module.news.presenter.INewsCollectionsPresenter;
import com.oushangfeng.ounews.module.news.presenter.INewsCollectionsPresenterImpl;
import com.oushangfeng.ounews.module.news.presenter.INewsListPresenterImpl;
import com.oushangfeng.ounews.module.news.presenter.INewsPresenter;
import com.oushangfeng.ounews.module.news.presenter.INewsPresenterImpl;
import com.oushangfeng.ounews.module.news.ui.adapter.NewsChannelAdapter;
import com.oushangfeng.ounews.module.news.ui.adapter.NewsListFragmentAdapter;
import com.oushangfeng.ounews.module.news.view.INewsCollectionsView;
import com.oushangfeng.ounews.module.news.view.INewsView;
import com.oushangfeng.ounews.module.photo.ui.PhotoDetailActivity;
import com.oushangfeng.ounews.utils.ClickUtils;
import com.oushangfeng.ounews.utils.GlideUtils;
import com.oushangfeng.ounews.utils.MeasureUtil;
import com.oushangfeng.ounews.utils.ViewUtil;
import com.oushangfeng.ounews.widget.ThreePointLoadingView;
import com.oushangfeng.ounews.widget.refresh.RefreshLayout;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

@ActivityFragmentInject(contentViewId = R.layout.activity_news_collections,
        menuId = R.menu.menu_settings,
        hasNavigationView = true,
        toolbarTitle = R.string.news_collection,
        toolbarIndicator = R.drawable.ic_list_white,
        menuDefaultCheckedItem = R.id.action_collection)
public class NewsCollectionsActivity extends BaseActivity<BasePresenter> implements INewsCollectionsView{
    //private mAdapter;

    private ThreePointLoadingView mLoadingView;
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private INewsCollectionsPresenter mPresenter;

    private BaseRecyclerAdapter<TsinghuaNewsSummary> mAdapter;

    private SinaPhotoDetail mSinaPhotoDetail;


    @Override
    protected void initView() {

        // 设了默认的windowBackground使得冷启动没那么突兀，这里再设置为空减少过度绘制
        getWindow().setBackgroundDrawable(null);
        ViewUtil.quitFullScreen(this);

        AppManager.getAppManager().orderNavActivity(getClass().getName(), false);

        mLoadingView = (ThreePointLoadingView) findViewById(R.id.tpl_view2);
        mLoadingView.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view2);

        mRefreshLayout = (RefreshLayout) findViewById(R.id.collections_layout);

        mPresenter = new INewsCollectionsPresenterImpl(this);

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
                String pic[] = item.pictures.split(";");
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
