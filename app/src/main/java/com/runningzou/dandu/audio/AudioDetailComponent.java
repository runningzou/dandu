package com.runningzou.dandu.audio;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by runningzou on 2017/10/31.
 */

@Subcomponent
public interface AudioDetailComponent extends AndroidInjector<AudioDetailActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AudioDetailActivity> {
    }
}
