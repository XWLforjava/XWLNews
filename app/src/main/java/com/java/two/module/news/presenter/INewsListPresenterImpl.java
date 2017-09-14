package com.java.two.module.news.presenter;

import com.java.two.base.BasePresenterImpl;
import com.java.two.bean.TsinghuaNewsSummary;
import com.java.two.common.DataLoadType;
import com.java.two.module.news.model.INewsListInteractor;
import com.java.two.module.news.model.INewsListInteractorImpl;
import com.java.two.module.news.view.INewsListView;

import java.util.List;

/**
 * ClassName: INewsListPresenterImpl<p>
 * Author: oubowu<p>
 * Fuction: 新闻列表代理接口实现<p>
 * CreateDate: 2016/2/18 14:39<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class INewsListPresenterImpl extends BasePresenterImpl<INewsListView, List<TsinghuaNewsSummary>> implements INewsListPresenter {

    private INewsListInteractor<List<TsinghuaNewsSummary>> mNewsListInteractor;
    private int mPageNo;
    private int mCategory;
    private int mPageSize;

    private boolean mIsRefresh = true;
    private boolean mHasInit;

    public INewsListPresenterImpl(INewsListView newsListView, int pageSize, int category) {
        super(newsListView);
        mNewsListInteractor = new INewsListInteractorImpl();
        mPageNo = 1;
        mSubscription = mNewsListInteractor.requestNewsList(this, mPageNo, pageSize, category);
        mCategory = category;
        mPageSize = pageSize;
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
        mSubscription = mNewsListInteractor.requestNewsList(this, mPageNo, mPageSize, mCategory);
    }

    @Override
    public void loadMoreData() {
        mIsRefresh = false;
        mSubscription = mNewsListInteractor.requestNewsList(this, mPageNo, mPageSize, mCategory);
    }

}
