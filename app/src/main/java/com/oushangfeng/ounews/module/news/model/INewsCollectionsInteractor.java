package com.oushangfeng.ounews.module.news.model;

import com.oushangfeng.ounews.callback.RequestCallback;

import rx.Subscription;

/**
 * ClassName: INewsListInteractor<p>
 * Author: oubowu<p>
 * Fuction: 新闻收藏列表Model层接口<p>
 * CreateDate: 2016/2/17 21:02<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsCollectionsInteractor<T> {

    Subscription requestNewsList(RequestCallback<T> callback);

}