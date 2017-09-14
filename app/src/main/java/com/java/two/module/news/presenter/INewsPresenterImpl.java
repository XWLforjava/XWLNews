package com.java.two.module.news.presenter;

import com.java.two.base.BasePresenterImpl;
import com.java.two.greendao.NewsChannelTable;
import com.java.two.module.news.model.INewsInteractor;
import com.java.two.module.news.model.INewsInteractorImpl;
import com.java.two.module.news.view.INewsView;

import java.util.List;

/**
 * ClassName: INewsPresenterImpl<p>
 * Author: oubowu<p>
 * Fuction: 新闻代理接口实现<p>
 * CreateDate: 2016/2/17 21:04<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class INewsPresenterImpl extends BasePresenterImpl<INewsView, List<NewsChannelTable>>
        implements INewsPresenter {

    private INewsInteractor<List<NewsChannelTable>> mNewsInteractor;

    public INewsPresenterImpl(INewsView newsView) {
        super(newsView);
        mNewsInteractor = new INewsInteractorImpl();
        mSubscription = mNewsInteractor.operateChannelDb(this);
        mView.initRxBusEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void requestSuccess(List<NewsChannelTable> data) {
        mView.initViewPager(data);
    }

    @Override
    public void operateChannelDb() {
        mSubscription = mNewsInteractor.operateChannelDb(this);
    }
}
