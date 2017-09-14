package com.java.two.module.news.view;

import android.support.annotation.NonNull;

import com.java.two.base.BaseView;
import com.java.two.bean.TsinghuaNewsSummary;
import com.java.two.common.DataLoadType;

import java.util.List;

/**
 * ClassName: INewsListView<p>
 * Author: oubowu<p>
 * Fuction: 新闻列表视图接口<p>
 * CreateDate: 2016/2/18 14:42<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsListView extends BaseView {

    void updateNewsList(List<TsinghuaNewsSummary> data, @NonNull String errorMsg, @DataLoadType.DataLoadTypeChecker int type);

}
