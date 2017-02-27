package ru.ifmo.practice;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

public class VKSmartFeedApplication extends Application {
    private static VKSmartFeedApplication   mApp;
    private static ConnectivityManager      mConnectivityManager;

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                System.out.println("ploho");
            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }

    public static Context getContext() {
        return mApp.getApplicationContext();
    }

    public static boolean isOnline() {
        NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
