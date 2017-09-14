package com.java.two.module.news.view;

import android.support.annotation.NonNull;

import com.java.two.base.BaseView;
import com.java.two.bean.TsinghuaNewsSummary;
import com.java.two.common.DataLoadType;

import java.util.List;

/**
 * ClassName: INewsView<p>
 * Author: oubowu<p>
 * Fuction: 新闻收藏视图接口<p>
 * CreateDate: 2016/2/17 20:25<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsCollectionsView extends BaseView {
    void updateNewsList(List<TsinghuaNewsSummary> data, @NonNull String errorMsg, @DataLoadType.DataLoadTypeChecker int type);

}
