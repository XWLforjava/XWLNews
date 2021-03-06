package com.java.two.module.settings.presenter;

import com.java.two.base.BasePresenterImpl;
import com.java.two.module.settings.view.ISettingsView;

/**
 * ClassName: <p>
 * Author: oubowu<p>
 * Fuction: <p>
 * CreateDate: 2016/2/27 0:17<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class ISettingsPresenterImpl extends BasePresenterImpl<ISettingsView, Void> implements ISettingsPresenter{

    public ISettingsPresenterImpl(ISettingsView view) {
        super(view);
        mView.initItemState();
    }
}
