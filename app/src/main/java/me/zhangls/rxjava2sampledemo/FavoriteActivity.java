package me.zhangls.rxjava2sampledemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        Observable.just(token)
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<String>>() {
                               @Override
                               public ObservableSource<String> apply
                                       (Observable<Throwable> throwableObservable) throws Exception {


                                   return throwableObservable.flatMap(new Function<Throwable, ObservableSource<String>>() {
                                       @Override
                                       public ObservableSource<String> apply(Throwable throwable) throws Exception {
                                           rx.Observable<Result<FavoriteActivity>> observable = RxActivityResult
                                                   .on(FavoriteActivity.this)
                                                   .startIntent(new Intent(FavoriteActivity.this, LoginActivity.class));

                                           if (throwable instanceof IllegalArgumentException
                                                   || throwable instanceof NullPointerException) {
                                               return RxJavaInterop
                                                       .toV2Observable(observable)
                                                       .map(new Function<Result<FavoriteActivity>, String>() {
                                                           @Override
                                                           public String apply(Result<FavoriteActivity> result) throws Exception {
                                                               return result.data().getStringExtra("token");
                                                           }
                                                       });
                                           }
                                           return Observable.error(throwable);
                                       }
                                   });

                               }
                           }

                )
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<String, String>() {
                         @Override
                         public String apply(String s) throws Exception {
                             Thread.sleep(500);
                             return s;
                         }
                     }

                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   Toast.makeText(FavoriteActivity.this, "ok", Toast.LENGTH_SHORT).show();
                               }
                           }

                );
    }


}
