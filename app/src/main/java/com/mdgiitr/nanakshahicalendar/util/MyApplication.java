package com.mdgiitr.nanakshahicalendar.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.android.gms.ads.MobileAds;

import io.realm.Realm;

/**
 * Created by SavviSingh on 27/01/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        Realm.init(this);
        MobileAds.initialize(this, "ca-app-pub-2995010605730030~9551781504");


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
