package com.runningzou.dandu.detail;

import com.runningzou.dandu.main.MainActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by runningzou on 2017/10/30.
 */

@Subcomponent
public interface DetailComponent extends AndroidInjector<DetailActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<DetailActivity> {
    }
}
