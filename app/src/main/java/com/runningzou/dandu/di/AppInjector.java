package com.runningzou.dandu.di;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.runningzou.dandu.app.DaggerAppComponent;
import com.runningzou.dandu.app.DanduApp;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;

/**
 * Created by runningzou on 2017/9/17.
 */

public class AppInjector {
    private AppInjector() {
    }

    public static void init(DanduApp app) {
        DaggerAppComponent.builder()
                .application(app)
                .build()
                .inject(app);

        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                handleActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private static void handleActivity(Activity activity) {

        if (activity instanceof Injector) {
            AndroidInjection.inject(activity);
        }


        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                        @Override
                        public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                            super.onFragmentCreated(fm, f, savedInstanceState);
                            if (f instanceof Injector)
                                AndroidSupportInjection.inject(f);
                        }
                    }, true);
        }
    }

}
