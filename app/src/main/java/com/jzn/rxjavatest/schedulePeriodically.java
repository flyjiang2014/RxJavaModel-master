package com.jzn.rxjavatest;

import android.app.Activity;
import android.os.Bundle;

import com.jzn.rxjavatest.utils.MyLog;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * schedulePeriodically
 * 轮询请求
 * Created by jiangzn on 16/9/9.
 */
public class schedulePeriodically extends Activity {
    private CompositeSubscription mCompositeSubscription
            = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      Subscription subscription =  Observable.create(new Observable.OnSubscribe<String>(
        ) {
            @Override
            public    void call(final Subscriber<? super String> subscriber) {
                Scheduler.Worker worker = Schedulers.io().createWorker();
                            //指定在io线程执行
                worker.schedulePeriodically(new Action0() {
                                @Override
                                public void call() {

                                    subscriber.onNext("doNetworkCallAndGetStringResult");

                                }
                            }, 5000, 2000, TimeUnit.MILLISECONDS);//初始延迟，polling延迟

                mCompositeSubscription.add(worker);
                }

        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                MyLog.d("polling..." + s);
            }
        });
        mCompositeSubscription.add(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.d("polling..." +"end");
        mCompositeSubscription.unsubscribe();
        //注意! 一旦你调用了 CompositeSubscription.unsubscribe()，
        // 这个CompositeSubscription对象就不可用了, 如果你还想使用CompositeSubscription，
        // 就必须在创建一个新的对象了。
    }

}
