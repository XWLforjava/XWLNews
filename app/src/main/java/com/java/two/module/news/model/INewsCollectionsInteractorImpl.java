package com.java.two.module.news.model;

import com.java.two.bean.TsinghuaNewsSummary;
import com.java.two.callback.RequestCallback;
import com.java.two.collection.CollectionManager;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

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


