package me.zhangls.rxjava2sampledemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.concurrent.Executors;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        threadInfo("OnSubscribe.call()");
                        subscriber.onNext("RxJava");
                    }
                })
                .subscribeOn(getNamedScheduler("create之后的subscribeOn"))
                .doOnSubscribe(
                        new Action0() {
                            @Override
                            public void call() {
                                threadInfo(".doOnSubscribe()-1");
                            }
                        }
                )
                .subscribeOn(getNamedScheduler("doOnSubscribe1之后的subscribeOn"))
                .doOnSubscribe(
                        new Action0() {
                            @Override
                            public void call() {
                                threadInfo(".doOnSubscribe()-2");
                            }
                        }
                )
                .subscribe(s -> {
                    threadInfo(".onNext()");
                    System.out.println(s + "-onNext");
                });
    }

    public void homeCheck(View view) {
        startActivity(new Intent(this, HomeCheckActivity.class));
    }

    public void favorite(View view) {
        startActivity(new Intent(this, FavoriteActivity.class));
    }


    public static Scheduler getNamedScheduler(String name) {
        return Schedulers.from(Executors.newCachedThreadPool(r -> new Thread(r, name)));
    }

    public static void threadInfo(String caller) {
        System.out.println(caller + " => " + Thread.currentThread().getName());
    }


}
