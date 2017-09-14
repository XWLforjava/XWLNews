package com.java.two.module.video.presenter;

import com.java.two.base.BasePresenterImpl;
import com.java.two.module.video.view.IVideoView;

/**
 * ClassName: IVideoPresenterImpl<p>
 * Author: oubowu<p>
 * Fuction: 视频代理接口实现<p>
 * CreateDate: 2016/2/23 16:42<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class IVideoPresenterImpl extends BasePresenterImpl<IVideoView, Void>
        implements IVideoPresenter {

    public IVideoPresenterImpl(IVideoView view) {
        super(view);
        mView.initViewPager();
    }


}
