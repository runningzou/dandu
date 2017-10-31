package com.runningzou.dandu.main;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by runningzou on 17-10-28.
 */

@Subcomponent
public interface MainActivityComponent extends AndroidInjector<MainActivity>  {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {
    }
}
