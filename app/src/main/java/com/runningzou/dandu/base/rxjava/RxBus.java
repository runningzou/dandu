package com.runningzou.dandu.base.rxjava;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by runningzou on 17-10-28.
 */

public class RxBus {

    private static volatile RxBus sInstance;

    public static RxBus getInstance() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    private Subject<Object> mSubject = PublishSubject.create().toSerialized();

    public void post(Object event) {
        mSubject.onNext(event);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mSubject.ofType(eventType);
    }

}