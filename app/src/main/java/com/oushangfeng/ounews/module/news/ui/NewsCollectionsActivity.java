package com.oushangfeng.ounews.module.news.ui;

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
import com.oushangfeng.ounews.callback.OnItemClickAdapter;
import com.oushangfeng.ounews.callback.SimpleItemTouchHelperCallback;
import com.oushangfeng.ounews.common.DataLoadType;
import com.oushangfeng.ounews.greendao.NewsChannelTable;
import com.oushangfeng.ounews.module.news.presenter.INewsCollectionsPresenterImpl;
import com.oushangfeng.ounews.module.news.presenter.INewsListPresenterImpl;
import com.oushangfeng.ounews.module.news.presenter.INewsPresenter;
import com.oushangfeng.ounews.module.news.presenter.INewsPresenterImpl;
import com.oushangfeng.ounews.module.news.ui.adapter.NewsChannelAdapter;
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

@ActivityFragmentInject(contentViewId = R.layout.activity_news,
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


    @Override
    protected void initView() {

        // 设了默认的windowBackground使得冷启动没那么突兀，这里再设置为空减少过度绘制
        getWindow().setBackgroundDrawable(null);
        ViewUtil.quitFullScreen(this);

        AppManager.getAppManager().orderNavActivity(getClass().getName(), false);

        mLoadingView = (ThreePointLoadingView) findViewById(R.id.tpl_view);
        mLoadingView.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

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
    public void updateNewsList(final List<NeteastNewsSummary> data, String errorMsg, @DataLoadType.DataLoadTypeChecker int type) {
        /*
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
        */
    }
}
