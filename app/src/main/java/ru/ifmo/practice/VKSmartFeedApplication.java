package ru.ifmo.practice;

import android.app.Application;
import android.content.Context;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

public class VKSmartFeedApplication extends Application {
    private static VKSmartFeedApplication mApp = null;

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {

            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }

    public static Context context()
    {
        return mApp.getApplicationContext();
    }
}
