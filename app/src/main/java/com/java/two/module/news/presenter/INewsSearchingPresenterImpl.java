package com.java.two.module.news.presenter;

import com.java.two.base.BasePresenterImpl;
import com.java.two.bean.TsinghuaNewsSummary;
import com.java.two.common.DataLoadType;
import com.java.two.module.news.model.INewsSearchingInteractor;
import com.java.two.module.news.model.INewsSearchingInteractorImpl;
import com.java.two.module.news.view.INewsSearchingView;

import java.util.List;

/**
 * ClassName: INewsListPresenterImpl<p>
 * Author: oubowu<p>
 * Fuction: 新闻搜索结果代理接口实现<p>
 * CreateDate: 2016/2/18 14:39<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class INewsSearchingPresenterImpl extends BasePresenterImpl<INewsSearchingView, List<TsinghuaNewsSummary>> implements INewsSearchingPresenter {

    private INewsSearchingInteractor<List<TsinghuaNewsSummary>> mNewsSearchingInteractor;
    private int mPageNo;
    private int mCategory;
    private int mPageSize;

    private boolean mIsRefresh = true;
    private boolean mHasInit;

    private String mKeywords;

    public INewsSearchingPresenterImpl(INewsSearchingView newsSearchingView, String keywords) {
        super(newsSearchingView);
        mNewsSearchingInteractor = new INewsSearchingInteractorImpl();
        mPageNo = 1;
        mKeywords = keywords;
        mSubscription = mNewsSearchingInteractor.requestNewsList(this, mPageNo, mKeywords);
    }

    @Override
    public void beforeRequest() {
        if (!mHasInit) {
            mHasInit = true;
            mView.showProgress();
        }
    }

    @Override
    public void requestError(String e) {
        super.requestError(e);
        mView.updateNewsList(null, e, mIsRefresh ? DataLoadType.TYPE_REFRESH_FAIL : DataLoadType.TYPE_LOAD_MORE_FAIL);
    }

    @Override
    public void requestSuccess(List<TsinghuaNewsSummary> data) {
        if (data != null) {
            mPageNo += 1;
        }
        mView.updateNewsList(data, "", mIsRefresh ? DataLoadType.TYPE_REFRESH_SUCCESS : DataLoadType.TYPE_LOAD_MORE_SUCCESS);

    }

    @Override
    public void refreshData() {
        mPageNo = 1;
        mIsRefresh = true;
        mSubscription = mNewsSearchingInteractor.requestNewsList(this, mPageNo, mKeywords);
    }

    @Override
    public void loadMoreData() {
        mIsRefresh = false;
        mSubscription = mNewsSearchingInteractor.requestNewsList(this, mPageNo, mKeywords);
    }

}
