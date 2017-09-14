package com.java.two.module.video.view;

import com.java.two.base.BaseView;
import com.java.two.bean.NeteastVideoSummary;
import com.java.two.common.DataLoadType;

import java.util.List;

/**
 * ClassName: IVideoListView<p>
 * Author: oubowu<p>
 * Fuction: 视频列表视图接口<p>
 * CreateDate: 2016/2/23 17:05<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IVideoListView extends BaseView {

    void updateVideoList(List<NeteastVideoSummary> data, String errorMsg, @DataLoadType.DataLoadTypeChecker int type);

}
