package com.java.two.module.news.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.two.base.BaseSubscriber;
import com.java.two.bean.TsinghuaNewsSummary;
import com.java.two.callback.RequestCallback;
import com.java.two.http.HostType;
import com.java.two.http.manager.RetrofitManager;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Func1;

/**
 * ClassName: INewsListInteractorImpl<p>
 * Author: oubowu<p>
 * Fuction: 新闻搜索结果列表Model层接口实现<p>
 * CreateDate: 2016/2/17 21:02<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class INewsSearchingInteractorImpl implements INewsSearchingInteractor<List<TsinghuaNewsSummary>> {

    @Override

    public Subscription requestNewsList(final RequestCallback<List<TsinghuaNewsSummary>> callback, int pageNo, String keywords) {
        KLog.e("新闻列表：" );
        return RetrofitManager.getInstance(HostType.TSINGHUA_NEWS)
                .searchTsinghuaNewsObservable(pageNo, 20, keywords)
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


