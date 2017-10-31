package com.runningzou.dandu.app;

import android.app.Application;

import com.runningzou.dandu.audio.AudioDetailComponent;
import com.runningzou.dandu.detail.DetailComponent;
import com.runningzou.dandu.main.MainActivityComponent;
import com.runningzou.dandu.model.net.ApiService;
import com.runningzou.dandu.model.net.StringConverterFactory;
import com.runningzou.dandu.splash.SplashActivityComponent;
import com.runningzou.dandu.video.VideoDetailComponent;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by runningzou on 17-10-27.
 */

@Module(subcomponents = {SplashActivityComponent.class,
        MainActivityComponent.class,
        DetailComponent.class,
        AudioDetailComponent.class,
        VideoDetailComponent.class
})
public class AppModule {

    @Singleton
    @Provides
    public Retrofit provideRetrofit(Application context) {

        File cacheFile = new File(context.getCacheDir(), "httpCache");
        Cache cache = new Cache(cacheFile, 10 * 1024 * 1024);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://static.owspace.com/")
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }


    @Singleton
    @Provides
    public ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

}
