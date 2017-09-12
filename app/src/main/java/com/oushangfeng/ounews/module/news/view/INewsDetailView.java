package com.oushangfeng.ounews.module.news.view;

import com.oushangfeng.ounews.base.BaseView;
import com.oushangfeng.ounews.bean.NeteastNewsDetail;
import com.oushangfeng.ounews.bean.TsinghuaNewsDetail;

/**
 * ClassName: INewsDetailView<p>
 * Author: oubowu<p>
 * Fuction: 新闻详情视图接口<p>
 * CreateDate: 2016/2/19 14:52<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsDetailView extends BaseView{

    void initNewsDetail(TsinghuaNewsDetail data);

}
