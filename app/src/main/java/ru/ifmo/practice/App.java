package ru.ifmo.practice;

import android.app.Application;
import android.content.res.Configuration;

import com.vk.sdk.VKSdk;

/**
 * Created by sstya on 04.01.2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
