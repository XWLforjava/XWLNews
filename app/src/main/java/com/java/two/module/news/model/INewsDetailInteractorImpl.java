package com.java.two.module.news.model;

import com.java.two.base.BaseSubscriber;
import com.java.two.bean.TsinghuaNewsDetail;
import com.java.two.callback.RequestCallback;
import com.java.two.http.HostType;
import com.java.two.http.manager.RetrofitManager;

import rx.Subscription;

/**
 * ClassName: INewsDetailInteractorImpl<p>
 * Author: oubowu<p>
 * Fuction: 新闻详情的Model层接口实现<p>
 * CreateDate: 2016/2/19 21:02<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class INewsDetailInteractorImpl implements INewsDetailInteractor<TsinghuaNewsDetail> {

    @Override
    public Subscription requestNewsDetail(final RequestCallback<TsinghuaNewsDetail> callback, final String id) {
        return RetrofitManager.getInstance(HostType.TSINGHUA_NEWS).getTsinghuaNewsDetailObservable(id)
                .subscribe(new BaseSubscriber<>(callback));
    }

}
