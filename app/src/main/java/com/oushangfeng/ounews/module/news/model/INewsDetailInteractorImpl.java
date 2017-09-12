package com.oushangfeng.ounews.module.news.model;

import com.oushangfeng.ounews.base.BaseSubscriber;
import com.oushangfeng.ounews.bean.NeteastNewsDetail;
import com.oushangfeng.ounews.bean.TsinghuaNewsDetail;
import com.oushangfeng.ounews.callback.RequestCallback;
import com.oushangfeng.ounews.http.HostType;
import com.oushangfeng.ounews.http.manager.RetrofitManager;

import java.util.Map;

import rx.Subscription;
import rx.functions.Func1;

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
