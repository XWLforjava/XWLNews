package com.java.two.module.news.presenter;

import com.java.two.base.BasePresenterImpl;
import com.java.two.bean.TsinghuaNewsDetail;
import com.java.two.module.news.model.INewsDetailInteractor;
import com.java.two.module.news.model.INewsDetailInteractorImpl;
import com.java.two.module.news.view.INewsDetailView;

/**
 * ClassName: INewsDetailPresenterImpl<p>
 * Author: oubowu<p>
 * Fuction: 新闻详情代理接口实现<p>
 * CreateDate: 2016/2/19 21:11<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class INewsDetailPresenterImpl extends BasePresenterImpl<INewsDetailView, TsinghuaNewsDetail>
        implements INewsDetailPresenter {

    public INewsDetailPresenterImpl(INewsDetailView newsDetailView, String postId) {
        super(newsDetailView);
        INewsDetailInteractor<TsinghuaNewsDetail> newsDetailInteractor = new INewsDetailInteractorImpl();
        mSubscription = newsDetailInteractor.requestNewsDetail(this, postId);
    }

    @Override
    public void requestSuccess(TsinghuaNewsDetail data) {
        mView.initNewsDetail(data);
    }

    @Override
    public void requestError(String msg) {
        super.requestError(msg);
        mView.toast(msg);
    }
}
