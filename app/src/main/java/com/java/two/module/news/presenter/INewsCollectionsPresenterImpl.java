package com.java.two.module.news.presenter;

import com.java.two.base.BasePresenterImpl;
import com.java.two.bean.TsinghuaNewsSummary;
import com.java.two.common.DataLoadType;
import com.java.two.module.news.model.INewsCollectionsInteractor;
import com.java.two.module.news.model.INewsCollectionsInteractorImpl;
import com.java.two.module.news.view.INewsCollectionsView;

import java.util.List;

/**
 * ClassName: INewsListPresenterImpl<p>
 * Author: oubowu<p>
 * Fuction: 新闻收藏列表代理接口实现<p>
 * CreateDate: 2016/2/18 14:39<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class INewsCollectionsPresenterImpl extends BasePresenterImpl<INewsCollectionsView, List<TsinghuaNewsSummary>> implements INewsCollectionsPresenter {

    private INewsCollectionsInteractor<List<TsinghuaNewsSummary>> mNewsCollectionsInteractor;

    private boolean mIsRefresh = true;
    private boolean mHasInit;

    public INewsCollectionsPresenterImpl(INewsCollectionsView newsListView) {
        super(newsListView);
        mNewsCollectionsInteractor = new INewsCollectionsInteractorImpl();
        mSubscription = mNewsCollectionsInteractor.requestNewsList(this);
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
        }
        mView.updateNewsList(data, "", mIsRefresh ? DataLoadType.TYPE_REFRESH_SUCCESS : DataLoadType.TYPE_LOAD_MORE_SUCCESS);

    }

    @Override
    public void refreshData() {
        mIsRefresh = true;
        mSubscription = mNewsCollectionsInteractor.requestNewsList(this);
    }

    @Override
    public void loadMoreData() {
        mIsRefresh = false;
        mSubscription = mNewsCollectionsInteractor.requestNewsList(this);
    }

}
