package com.java.two.module.photo.presenter;

import com.java.two.base.BasePresenterImpl;
import com.java.two.bean.SinaPhotoDetail;
import com.java.two.module.photo.model.IPhotoDetailInteractor;
import com.java.two.module.photo.model.IPhotoDetailInteractorImpl;
import com.java.two.module.photo.view.IPhotoDetailView;

/**
 * ClassName: IPhotoDetailPresenterImpl<p>
 * Author: oubowu<p>
 * Fuction: 图片详情代理接口实现<p>
 * CreateDate: 2016/2/22 17:46<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class IPhotoDetailPresenterImpl extends BasePresenterImpl<IPhotoDetailView, SinaPhotoDetail>
        implements IPhotoDetailPresenter {

    private IPhotoDetailInteractor<SinaPhotoDetail> mDetailInteractor;

    public IPhotoDetailPresenterImpl(IPhotoDetailView view, String id, SinaPhotoDetail data) {
        super(view);
        mDetailInteractor = new IPhotoDetailInteractorImpl();
        if (data != null) {
            mView.initViewPager(data);
        } else {
            mSubscription = mDetailInteractor.requestPhotoDetail(this, id);
        }
    }

    @Override
    public void requestSuccess(SinaPhotoDetail data) {
        mView.initViewPager(data);
    }

    @Override
    public void requestError(String msg) {
        super.requestError(msg);
        mView.toast(msg);
    }
}
