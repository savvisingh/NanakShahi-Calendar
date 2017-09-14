package com.mdgiitr.nanakshahicalendar.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

/**
 * Created by SavviSingh on 27/01/17.
 */

public class MyApplication extends Application {

    private static FirebaseAnalytics mFireBaseAnalytics;

    @Override
    public void onCreate() {

        super.onCreate();
        Realm.init(this);
        MobileAds.initialize(this, "ca-app-pub-2995010605730030~9551781504");
        Fabric.with(this, new Crashlytics());
        mFireBaseAnalytics = FirebaseAnalytics.getInstance(this);


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static FirebaseAnalytics getFireBaseAnalyticsInstance(){
        return mFireBaseAnalytics;
    }
}
