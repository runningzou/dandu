package com.runningzou.dandu.splash;

import android.arch.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.runningzou.dandu.app.DanduApp;
import com.runningzou.dandu.base.rxjava.RxCommand;
import com.runningzou.dandu.model.entity.SplashImg;
import com.runningzou.dandu.model.net.ApiService;
import com.runningzou.dandu.util.AppUtil;
import com.runningzou.dandu.util.FileUtil;
import com.runningzou.dandu.util.TimeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by runningzou on 17-10-27.
 */

@Singleton
public class SplashViewModel extends ViewModel {

    private ApiService mApiService;
    private RxCommand<Object, Object> mGetSplashImgRxCommand;
    private RxCommand<List<String>, Object> mShowSplashRxCommand;

    @Inject
    public SplashViewModel(ApiService apiService) {
        this.mApiService = apiService;
    }

    public RxCommand<Object, Object> getSplashImgRxCommand() {
        if (mGetSplashImgRxCommand == null) {
            mGetSplashImgRxCommand = RxCommand.create(new Function<Object, Observable<Object>>() {
                @Override
                public Observable<Object> apply(@NonNull Object o) throws Exception {
                    String client = "android";
                    String appVersion = AppUtil.getAppVersionName(DanduApp.getApplication());
                    Long time = TimeUtil.getCurrentSeconds();
                    String deviceId = AppUtil.getDeviceId(DanduApp.getApplication());
                    return mApiService.getSplash(client, appVersion, time, deviceId)
                            .map(new Function<SplashImg, Object>() {
                                @Override
                                public Object apply(@NonNull SplashImg splashImg) throws Exception {

                                    final List<String> images = splashImg.getImages();

                                    for (final String url : images) {
                                        Logger.d(url);
                                        File file = Glide.with(DanduApp.getApplication())
                                                .asFile()
                                                .load(url)
                                                .submit()
                                                .get();

                                        try {

                                            FileUtil.createSdDir();
                                            File targetFile = FileUtil.createFile(String.valueOf(images.indexOf(url)));

                                            FileOutputStream outputStream = new FileOutputStream(targetFile);
                                            FileInputStream inputStream = new FileInputStream(file);

                                            byte[] buffer = new byte[1024];
                                            int len = 0;
                                            while ((len = (inputStream.read(buffer))) != -1) {
                                                outputStream.write(buffer,0,len);
                                            }

                                            outputStream.flush();
                                            outputStream.close();
                                            inputStream.close();

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                    return null;
                                }
                            })
                            .subscribeOn(Schedulers.io());

                }
            });
        }

        return mGetSplashImgRxCommand;
    }

    public RxCommand<List<String>, Object> showSplashRxCommand() {

        if (mShowSplashRxCommand == null) {
            mShowSplashRxCommand = RxCommand.create(new Function<Object, Observable<List<String>>>() {
                @Override
                public Observable<List<String>> apply(@NonNull Object o) throws Exception {

                    return Observable.create(new ObservableOnSubscribe<List<String>>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                            FileUtil.createSdDir();
                            List<String> imgs = FileUtil.getAllAD();
                            e.onNext(imgs);
                            e.onComplete();
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            });
        }

        return mShowSplashRxCommand;
    }


}
