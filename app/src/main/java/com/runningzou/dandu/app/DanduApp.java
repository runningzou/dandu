package com.runningzou.dandu.app;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.runningzou.dandu.R;
import com.runningzou.dandu.di.AppInjector;
import com.runningzou.dandu.util.AppUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by runningzou on 17-10-27.
 */

public class DanduApp extends Application implements HasActivityInjector {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static Application sApplication;
    private static String mDeviceId;

    @Inject
    DispatchingAndroidInjector<Activity> mDispatchingAndroidInjector;


    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        AppUtil.getDeviceId(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        initTypeFace();
        AppInjector.init(this);
    }

    private void initTypeFace() {
        CalligraphyConfig calligraphyConfig =new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/PMingLiU.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
        CalligraphyConfig.initDefault(calligraphyConfig);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mDispatchingAndroidInjector;
    }

    public static Application getApplication() {
        return sApplication;
    }

    public static String getmDeviceId() {
        return mDeviceId;
    }
}
