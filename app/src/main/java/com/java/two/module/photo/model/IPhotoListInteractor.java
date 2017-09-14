package com.java.two.module.photo.model;

import com.java.two.callback.RequestCallback;

import rx.Subscription;

/**
 * ClassName: IPhotoListInteractor<p>
 * Author: oubowu<p>
 * Fuction: 图片列表Model层接口<p>
 * CreateDate: 2016/2/21 3:48<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IPhotoListInteractor<T> {

    Subscription requestPhotoList(RequestCallback<T> callback, String id, int startPage);

}
