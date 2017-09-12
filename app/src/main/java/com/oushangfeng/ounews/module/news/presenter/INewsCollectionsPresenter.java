package com.oushangfeng.ounews.module.news.presenter;

import com.oushangfeng.ounews.base.BasePresenter;

/**
 * ClassName: INewsListPresenter<p>
 * Author: oubowu<p>
 * Fuction: 新闻收藏列表代理接口<p>
 * CreateDate: 2016/2/18 14:39<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsCollectionsPresenter extends BasePresenter{

    void refreshData();

    void loadMoreData();

}