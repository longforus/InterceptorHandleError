package com.longforus.resulthandle.net;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 包装被观察者Scheduler
 * @param <T>
 */
public class SchedulerTransformer<T> implements Observable.Transformer<T, T> {
    @Override
    public Observable<T> call(Observable<T> observable) {
        return observable
                .subscribeOn(Schedulers.io())//执行在io线程
                .observeOn(AndroidSchedulers.mainThread());//观察在主线程
    }

    public static <T> SchedulerTransformer<T> create() {
        return new SchedulerTransformer<>();
    }

    private static SchedulerTransformer instance = null;

    private SchedulerTransformer(){
    }
    /**
     * 双重校验锁单例(线程安全)
     */
    public static <T> SchedulerTransformer<T> getInstance() {
        if (instance == null) {
            synchronized (SchedulerTransformer.class) {
                if (instance == null) {
                    instance = new SchedulerTransformer<>();
                }
            }
        }
        return instance;
    }
}