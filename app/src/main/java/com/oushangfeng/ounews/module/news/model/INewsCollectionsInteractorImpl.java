package com.oushangfeng.ounews.module.news.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.ThrowableDeserializer;
import com.oushangfeng.ounews.base.BaseSubscriber;
import com.oushangfeng.ounews.bean.NeteastNewsSummary;
import com.oushangfeng.ounews.bean.TsinghuaNewsSummary;
import com.oushangfeng.ounews.callback.RequestCallback;
import com.oushangfeng.ounews.collection.CollectionManager;
import com.oushangfeng.ounews.http.Api;
import com.oushangfeng.ounews.http.HostType;
import com.oushangfeng.ounews.http.manager.RetrofitManager;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import xwlnews.News;

/**
 * ClassName: INewsCollectionsInteractorImpl<p>
 * Author: oubowu<p>
 * Fuction: 新闻收藏列表Model层接口实现<p>
 * CreateDate: 2016/2/17 21:02<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class INewsCollectionsInteractorImpl implements INewsCollectionsInteractor<List<TsinghuaNewsSummary>> {

    @Override

    public Subscription requestNewsList(final RequestCallback<List<TsinghuaNewsSummary>> callback) {
        KLog.e("新闻列表");
        return Observable.create(new Observable.OnSubscribe<ArrayList<TsinghuaNewsSummary>>() {
            @Override
            public void call(Subscriber<? super ArrayList<TsinghuaNewsSummary>> subscriber) {
                subscriber.onNext(CollectionManager.getInstance().getCollectionsList());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        callback.beforeRequest();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<TsinghuaNewsSummary>>() {
                    @Override
                    public void onCompleted() {
                        callback.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.getLocalizedMessage() + "\n" + e);
                        callback.requestError(e.getLocalizedMessage() + "\n" + e);
                    }

                    @Override
                    public void onNext(ArrayList<TsinghuaNewsSummary> newslist) {
                        callback.requestSuccess(newslist);
                    }
                });
    }

}


