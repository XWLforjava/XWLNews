package com.oushangfeng.ounews.module.news.view;

import android.support.annotation.NonNull;

import com.oushangfeng.ounews.base.BaseView;
import com.oushangfeng.ounews.bean.NeteastNewsSummary;
import com.oushangfeng.ounews.bean.TsinghuaNewsSummary;
import com.oushangfeng.ounews.common.DataLoadType;
import com.oushangfeng.ounews.greendao.NewsChannelTable;

import java.util.List;

/**
 * ClassName: INewsView<p>
 * Author: oubowu<p>
 * Fuction: 新闻搜索视图接口<p>
 * CreateDate: 2016/2/17 20:25<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsSearchView extends BaseView {
    void updateNewsList(List<TsinghuaNewsSummary> data, @NonNull String errorMsg, @DataLoadType.DataLoadTypeChecker int type);
}