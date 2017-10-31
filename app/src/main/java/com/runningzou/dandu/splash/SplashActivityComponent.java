package com.runningzou.dandu.splash;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by runningzou on 17-10-27.
 */

@Subcomponent
public interface SplashActivityComponent extends AndroidInjector<SplashActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<SplashActivity> {
    }
}
