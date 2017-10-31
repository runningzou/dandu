package com.runningzou.dandu.app;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Created by runningzou on 17-10-27.
 */

@Singleton
@Component(
        modules = {
                AppModule.class,
                AndroidInjectorBuilder.class
        }
)
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(DanduApp app);
}
