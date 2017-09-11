package com.oushangfeng.ounews.module.news.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.app.AppManager;
import com.oushangfeng.ounews.base.BaseActivity;
import com.oushangfeng.ounews.base.BaseRecyclerAdapter;
import com.oushangfeng.ounews.base.BaseRecyclerViewHolder;
import com.oushangfeng.ounews.base.BaseSpacesItemDecoration;
import com.oushangfeng.ounews.bean.NeteastNewsSummary;
import com.oushangfeng.ounews.bean.SinaPhotoDetail;
import com.oushangfeng.ounews.callback.OnItemClickAdapter;
import com.oushangfeng.ounews.callback.SimpleItemTouchHelperCallback;
import com.oushangfeng.ounews.greendao.NewsChannelTable;
import com.oushangfeng.ounews.module.news.presenter.INewsListPresenterImpl;
import com.oushangfeng.ounews.module.news.presenter.INewsPresenter;
import com.oushangfeng.ounews.module.news.presenter.INewsPresenterImpl;
import com.oushangfeng.ounews.module.news.ui.adapter.NewsChannelAdapter;
import com.oushangfeng.ounews.module.news.view.INewsView;
import com.oushangfeng.ounews.utils.ClickUtils;
import com.oushangfeng.ounews.utils.GlideUtils;
import com.oushangfeng.ounews.utils.MeasureUtil;
import com.oushangfeng.ounews.utils.ViewUtil;
import com.oushangfeng.ounews.widget.ThreePointLoadingView;
import com.oushangfeng.ounews.widget.refresh.RefreshLayout;

import java.util.List;

@ActivityFragmentInject(contentViewId = R.layout.activity_news,
        menuId = R.menu.menu_settings,
        hasNavigationView = true,
        toolbarTitle = R.string.news_collection,
        toolbarIndicator = R.drawable.ic_list_white,
        menuDefaultCheckedItem = R.id.action_news)
public class NewsCollectionsActivity extends BaseActivity<INewsPresenter>{
    private BaseRecyclerAdapter<NeteastNewsSummary> mAdapter;
    private RecyclerView mRecyclerView;

    private SinaPhotoDetail mSinaPhotoDetail;


    @Override
    protected void initView() {
        getWindow().setBackgroundDrawable(null);
        ViewUtil.quitFullScreen(this);

        AppManager.getAppManager().orderNavActivity(getClass().getName(), false);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //mPresenter = new INewsPresenterImpl(this);
        //。。。从数据库中读取
        initRecyclerView(null);
    }
    public void initRecyclerView(List<NeteastNewsSummary> data) {
        /*
        mAdapter = new BaseRecyclerAdapter<NeteastNewsSummary>(getActivity(), data) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_news_summary;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, int position, NeteastNewsSummary item) {
                GlideUtils.loadDefault(item.imgsrc, holder.getImageView(R.id.iv_news_summary_photo), null, null, DiskCacheStrategy.RESULT);
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
                holder.getTextView(R.id.tv_news_summary_digest).setText(item.digest);
                holder.getTextView(R.id.tv_news_summary_ptime).setText(item.ptime);
            }
        };


        //mPresenter = new INewsListPresenterImpl(this, mNewsId, mNewsType);
        */
    }

}
