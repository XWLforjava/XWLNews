package com.oushangfeng.ounews.module.news.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oushangfeng.ounews.base.BaseSubscriber;
import com.oushangfeng.ounews.bean.NeteastNewsSummary;
import com.oushangfeng.ounews.bean.TsinghuaNewsSummary;
import com.oushangfeng.ounews.callback.RequestCallback;
import com.oushangfeng.ounews.http.Api;
import com.oushangfeng.ounews.http.HostType;
import com.oushangfeng.ounews.http.manager.RetrofitManager;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;
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

    public Subscription requestNewsList(final RequestCallback<List<TsinghuaNewsSummary>> callback, int pageNo, int pageSize, int category) {
        KLog.e("新闻列表：" + category);
        return RetrofitManager.getInstance(HostType.TSINGHUA_NEWS)
                .getTsinghuaNewsListObservable(pageNo, pageSize, category)
                .map(new Func1<JsonNode, ArrayList<TsinghuaNewsSummary>>() {
                         @Override
                         public ArrayList<TsinghuaNewsSummary> call(JsonNode node) {
                             ObjectMapper mapper = new ObjectMapper();
                             ArrayList list = null;
                             try
                             {
                                 String s = mapper.writeValueAsString(node.get("list"));
                                 list = mapper.readValue(s, new TypeReference<ArrayList<TsinghuaNewsSummary>>(){});
                             }
                             catch(Exception e){}
                             return list;
                         }
                     }
                )
                .subscribe(new BaseSubscriber<>(callback));
    }
}


