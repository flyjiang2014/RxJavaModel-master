package com.jzn.rxjavatest;

import android.app.Activity;
import android.os.Bundle;

import com.jzn.rxjavatest.utils.MyLog;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * interval
 * 做周期性操作
 * 每两秒输出"啪啪啪"
 * Created by jiangzn on 16/9/9.
 */
public class interval extends Activity {
    private CompositeSubscription mCompositeSubscription
            = new CompositeSubscription();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Subscription subscription =  Observable.interval(2, TimeUnit.SECONDS).take(5)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        MyLog.d("completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        MyLog.d("error");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        MyLog.d("啪啪啪");
                    }
                });
        MyLog.d("polling..." +"onCreate");
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
