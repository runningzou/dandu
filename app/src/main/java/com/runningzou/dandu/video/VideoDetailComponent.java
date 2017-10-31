package com.runningzou.dandu.video;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by runningzou on 2017/10/31.
 */

@Subcomponent
public interface VideoDetailComponent extends AndroidInjector<VideoDetailActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<VideoDetailActivity> {

    }
}
