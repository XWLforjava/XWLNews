package com.java.two.module.video.presenter;

import com.java.two.base.BasePresenterImpl;
import com.java.two.module.video.view.IVideoPlayView;

/**
 * ClassName: IVideoPlayPresenterImpl<p>
 * Author: oubowu<p>
 * Fuction: 视频播放代理接口实现<p>
 * CreateDate: 2016/2/23 21:32<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class IVideoPlayPresenterImpl extends BasePresenterImpl<IVideoPlayView, Void> implements IVideoPlayPresenter {

    public IVideoPlayPresenterImpl(IVideoPlayView view, String path, String name) {
        super(view);
        // mView.registerScreenBroadCastReceiver();
        mView.playVideo(path, name);
        mView.showProgress();
    }
}
