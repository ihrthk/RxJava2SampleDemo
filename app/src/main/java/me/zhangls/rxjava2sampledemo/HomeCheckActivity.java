package me.zhangls.rxjava2sampledemo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class HomeCheckActivity extends AppCompatActivity {

    private FrameLayout fragment_home_guide;
    private CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_check);

        fragment_home_guide = (FrameLayout) findViewById(R.id.fragment_home_guide);

        disposables = new CompositeDisposable();


        check(HomeCheckActivity.this);
    }


    private void check(final Context context) {
        //TODO rxpreference 发送两次
        Disposable disposable = Observable
                //返回：是否为第一次安装
                .create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                        e.onNext(PreferenceManager
                                .getDefaultSharedPreferences(context)
                                .getBoolean("isFirst", true));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //返回：是否为第一次安装
                .flatMap(new Function<Boolean, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                @Override
                                public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
                                    alphaAnimation.setDuration(2000);
                                    alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            if (!e.isDisposed()) {
                                                e.onNext(true);
                                            }
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                    fragment_home_guide.setVisibility(View.VISIBLE);
                                    fragment_home_guide.startAnimation(alphaAnimation);
                                }
                            });
                        } else {
                            return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                @Override
                                public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                                    e.onNext(false);
                                }
                            });
                        }
                    }
                })
                //返回：是否为第一次安装
                .flatMap(new Function<Boolean, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                @Override
                                public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                                    fragment_home_guide.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (!e.isDisposed()) {
                                                e.onNext(true);
                                            }
                                        }
                                    });
                                    e.setDisposable(new Disposable() {
                                        @Override
                                        public void dispose() {
                                            fragment_home_guide.setOnClickListener(null);
                                        }

                                        @Override
                                        public boolean isDisposed() {
                                            return true;
                                        }
                                    });
//                                e.add(new MainThreadSubscription() {
//                                    @Override
//                                    protected void onUnsubscribe() {
//                                        fragment_home_guide.setOnClickListener(null);
//                                    }
//                                });
                                }
                            });
                        } else {
                            return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                @Override
                                public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                                    e.onNext(false);
                                }
                            });
                        }
                    }
                })
                //返回：是否为第一次安装
                .flatMap(new Function<Boolean, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                @Override
                                public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                                    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
                                    alphaAnimation.setDuration(2000);
                                    alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            if (!e.isDisposed()) {
                                                e.onNext(true);
                                            }
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                    fragment_home_guide.setVisibility(View.GONE);
                                    fragment_home_guide.startAnimation(alphaAnimation);
                                }
                            });
                        } else {
                            return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                @Override
                                public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                                    e.onNext(false);
                                }
                            });
                        }
                    }
                })
                .observeOn(Schedulers.io())
                //返回：是否为第一次安装
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            PreferenceManager
                                    .getDefaultSharedPreferences(context)
                                    .edit()
                                    .putBoolean("isFirst", false)
                                    .apply();
                        }
                        return aBoolean;
                    }
                })
                //返回：最新版本的信息
                .flatMap(new Function<Boolean, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Boolean aBoolean) throws Exception {
                        Thread.sleep(300);

                        return new Observable<String>() {
                            @Override
                            protected void subscribeActual(Observer<? super String> observer) {
                                observer.onNext("update info");
                            }
                        };
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                //返回：是否版本更新
                .flatMap(new Function<String, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(String string) throws Exception {
                        if (string == null) {
                            return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                @Override
                                public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                                    e.onNext(false);
                                }
                            });
                        } else {
                            return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                @Override
                                public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                                    new AlertDialog.Builder(context)
                                            .setTitle("发现新版本")
                                            .setMessage("更新内容")
                                            .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            })
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            })
                                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialogInterface) {
                                                    e.onNext(true);
                                                }
                                            })
                                            .create().show();
                                }
                            });
                        }
                    }
                })
                .observeOn(Schedulers.io())
                //返回：弹窗工具信息
                .flatMap(new Function<Boolean, ObservableSource<String>>() {

                    @Override
                    public ObservableSource<String> apply(Boolean aBoolean) throws Exception {
                        Thread.sleep(300);
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> e) throws Exception {
                                e.onNext("ok");
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(HomeCheckActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });

        disposables.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }

}
