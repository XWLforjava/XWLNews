package com.java.two.module.news.model;

import com.java.two.callback.RequestCallback;

import rx.Subscription;

/**
 * ClassName: INewsListInteractor<p>
 * Author: oubowu<p>
 * Fuction: 新闻搜索结果列表Model层接口<p>
 * CreateDate: 2016/2/17 21:02<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsSearchingInteractor<T> {

    Subscription requestNewsList(RequestCallback<T> callback, int pageNo, String keywords);

}
