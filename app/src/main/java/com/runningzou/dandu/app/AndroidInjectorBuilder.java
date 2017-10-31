package com.runningzou.dandu.app;

import android.app.Activity;

import com.runningzou.dandu.audio.AudioDetailActivity;
import com.runningzou.dandu.audio.AudioDetailComponent;
import com.runningzou.dandu.detail.DetailActivity;
import com.runningzou.dandu.detail.DetailComponent;
import com.runningzou.dandu.main.MainActivity;
import com.runningzou.dandu.main.MainActivityComponent;
import com.runningzou.dandu.splash.SplashActivity;
import com.runningzou.dandu.splash.SplashActivityComponent;
import com.runningzou.dandu.video.VideoDetailActivity;
import com.runningzou.dandu.video.VideoDetailComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * Created by runningzou on 17-10-27.
 */

@Module
public abstract class AndroidInjectorBuilder {

    @Binds
    @IntoMap
    @ActivityKey(SplashActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindSplashActivity(SplashActivityComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivity(MainActivityComponent.Builder builder);


    @Binds
    @IntoMap
    @ActivityKey(DetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindDetailActivity(DetailComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(AudioDetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindAudioDetailActivity(AudioDetailComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(VideoDetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindVideoDetailActivity(VideoDetailComponent.Builder builder);
}
