package com.runningzou.dandu.splash;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.runningzou.dandu.R;
import com.runningzou.dandu.base.rxjava.Live;
import com.runningzou.dandu.databinding.ActivitySplashBinding;
import com.runningzou.dandu.di.Injector;
import com.runningzou.dandu.main.MainActivity;
import com.runningzou.dandu.util.NetUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class SplashActivity extends AppCompatActivity implements Injector {

    @Inject
    SplashViewModel mViewModel;

    private ActivitySplashBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemUi();
        mBinding = DataBindingUtil.setContentView(SplashActivity.this, R.layout.activity_splash);

        mViewModel.showSplashRxCommand()
                .switchToLatest()
                .compose(Live.<List<String>>bindLifecycle(this))
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        Logger.d(strings.size());
                        if (strings.size() == 0) {
                            Glide.with(SplashActivity.this)
                                    .load(R.drawable.welcome_default)
                                    .into(mBinding.splashImg);
                            if (NetUtil.isWifi(SplashActivity.this)) {
                                mViewModel.getSplashImgRxCommand().execute(null);
                            } else {
                                //非 wifi 环境，不下载图片
                            }

                        } else {
                            Random random = new Random();
                            int index = random.nextInt(strings.size());
                            File file = new File(strings.get(index));
                            Glide.with(SplashActivity.this)
                                    .load(file)
                                    .into(mBinding.splashImg);
                        }

                        showAnimator();
                    }
                });


        RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instanc

        if (rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) && rxPermissions.isGranted(Manifest.permission.READ_PHONE_STATE)) {
            mViewModel.showSplashRxCommand().execute(null);
        } else {
            rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) throws Exception {
                            if (permission.granted) {
                                Logger.d("permission granted");
                                mViewModel.showSplashRxCommand().execute(null);

                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // Denied permission without ask never again
                                Logger.d("Denied permission without ask never again");
                            } else {
                                // Denied permission with ask never again
                                // Need to go to the settings
                                Logger.d("Denied permission with ask never again");
                            }
                        }
                    });
        }



    }

    private void initSystemUi() {
        //布局延伸到状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }


    private void showAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBinding.splashImg, "translationX", -100F);
        animator.setDuration(1800L).start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
