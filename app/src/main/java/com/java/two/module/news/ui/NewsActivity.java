package com.java.two.module.news.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.EditText;

import com.java.two.R;
import com.java.two.annotation.ActivityFragmentInject;
import com.java.two.app.AppManager;
import com.java.two.base.BaseActivity;
import com.java.two.base.BaseFragment;
import com.java.two.base.BaseFragmentAdapter;
import com.java.two.greendao.NewsChannelTable;
import com.java.two.module.news.presenter.INewsPresenter;
import com.java.two.module.news.presenter.INewsPresenterImpl;
import com.java.two.module.news.view.INewsView;
import com.java.two.utils.RxBus;
import com.java.two.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * ClassName: NewsActivity<p>
 * Author: oubowu<p>
 * Fuction: 新闻界面<p>
 * CreateDate: 2016/2/20 2:12<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_news,
        menuId = R.menu.menu_news,
        hasNavigationView = true,
        toolbarTitle = R.string.news,
        toolbarIndicator = R.drawable.ic_list_white,
        menuDefaultCheckedItem = R.id.action_news)
public class NewsActivity extends BaseActivity<INewsPresenter> implements INewsView {

    private Observable<Boolean> mChannelObservable;
    private Context mContext;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister("channelChange", mChannelObservable);
    }

    @Override
    protected void initView() {

        // 设了默认的windowBackground使得冷启动没那么突兀，这里再设置为空减少过度绘制
        getWindow().setBackgroundDrawable(null);
        ViewUtil.quitFullScreen(this);

        AppManager.getAppManager().orderNavActivity(getClass().getName(), false);

        mPresenter = new INewsPresenterImpl(this);
        mContext = this;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_channel_manage) {
            //  跳转到频道选择界面
            showActivity(this, new Intent(this, NewsChannelActivity.class));
        }
        else if(item.getItemId() == R.id.action_search_news){
            final EditText inputServer = new EditText(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("News Search").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                    .setNegativeButton("Cancel", null);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    //toast(inputServer.getText().toString());
                    Intent intent = new Intent(mContext, NewsSearchingActivity.class);
                    intent.putExtra("Keywords", inputServer.getText().toString());
                    startActivity(intent);
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initViewPager(List<NewsChannelTable> newsChannels) {

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        List<BaseFragment> fragments = new ArrayList<>();
        final List<String> title = new ArrayList<>();
        //获取选中的chanels
        if (newsChannels != null) {
            // 有除了固定的其他频道被选中，添加
            for (NewsChannelTable news : newsChannels) {
                final NewsListFragment fragment = NewsListFragment
                        .newInstance(news.getCategory(),
                                news.getNewsChannelIndex());

                fragments.add(fragment);
                title.add(news.getNewsChannelName());
            }

            if (viewPager.getAdapter() == null) {
                // 初始化ViewPager
                BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager(),
                        fragments, title);
                viewPager.setAdapter(adapter);
            } else {
                final BaseFragmentAdapter adapter = (BaseFragmentAdapter) viewPager.getAdapter();
                adapter.updateFragments(fragments, title);
            }
            viewPager.setCurrentItem(0, false);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setScrollPosition(0, 0, true);
            // 根据Tab的长度动态设置TabLayout的模式
            ViewUtil.dynamicSetTabLayoutMode(tabLayout);

            setOnTabSelectEvent(viewPager, tabLayout);

        } else {
            toast("数据异常");
        }

    }

    @Override
    public void initRxBusEvent() {
        mChannelObservable = RxBus.get().register("channelChange", Boolean.class);
        mChannelObservable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean change) {
                if (change) {
                    mPresenter.operateChannelDb();
                }
            }
        });
    }


}
