package me.zhangls.rxjava2sampledemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rx_activity_result.Result;
import rx_activity_result.RxActivityResult;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        check();

    }

    private void check() {
        String token = null;

        Observable.just("")
                .flatMap(new Function<String, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(String s) throws Exception {
                        if (TextUtils.isEmpty(s)) {
                            rx.Observable<Result<FavoriteActivity>> observable = RxActivityResult
                                    .on(FavoriteActivity.this)
                                    .startIntent(new Intent(FavoriteActivity.this, LoginActivity.class));

                            return RxJavaInterop
                                    .toV2Observable(observable)
                                    .map(new Function<Result<FavoriteActivity>, Boolean>() {
                                        @Override
                                        public Boolean apply(Result<FavoriteActivity> result) throws Exception {
                                            return result.resultCode() == Activity.RESULT_OK;
                                        }
                                    });
                        } else {
                            return Observable.just(true);
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<Boolean, String>() {
                         @Override
                         public String apply(Boolean aBoolean) throws Exception {
                             Thread.sleep(500);
                             return aBoolean ? "success" : "failure";
                         }
                     }

                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   Toast.makeText(FavoriteActivity.this, s, Toast.LENGTH_SHORT).show();
                               }
                           }
                );
    }


}
