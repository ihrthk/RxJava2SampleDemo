package me.zhangls.rxjava2sampledemo;

import android.app.Application;

import rx_activity_result.RxActivityResult;

/**
 * Created by zhangls on 16/01/2017.
 */

public class AppContext extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RxActivityResult.register(this);
    }
}
