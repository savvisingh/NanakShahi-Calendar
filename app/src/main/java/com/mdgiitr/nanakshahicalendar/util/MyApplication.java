package com.savvisingh.nanakshahicalendar.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by SavviSingh on 27/01/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        Realm.init(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
