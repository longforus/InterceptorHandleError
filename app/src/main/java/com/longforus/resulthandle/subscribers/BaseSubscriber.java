package com.longforus.resulthandle.subscribers;

import com.longforus.resulthandle.exception.ResultException;
import rx.Subscriber;

/**
 * Created by Void Young on 11/11/2016  8:56 PM.
 * Description : 简单抽离onError  用自定义的exception 代替了原来的throwable 给子类继承
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {
    /**
     * 如果发生error就把exception交给自定义的onError执行
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (e instanceof ResultException) {
            onError((ResultException) e);
        } else {
            onError(new ResultException(e));
        }
    }

    /**
     * 使用自定义exception的onError 错误回调到这里
     * @param e
     */
    protected abstract void onError(ResultException e);
}
