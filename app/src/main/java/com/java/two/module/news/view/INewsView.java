package com.java.two.module.news.view;

import com.java.two.base.BaseView;
import com.java.two.greendao.NewsChannelTable;

import java.util.List;

/**
 * ClassName: INewsView<p>
 * Author: oubowu<p>
 * Fuction: 新闻视图接口<p>
 * CreateDate: 2016/2/17 20:25<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsView extends BaseView {

    void initViewPager(List<NewsChannelTable> newsChannels);

    void initRxBusEvent();

}
