package com.mdgiitr.nanakshahicalendar.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mdgiitr.nanakshahicalendar.model.DayMonth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by SavviSingh on 27/01/17.
 */

public class MyApplication extends Application {

    private static FirebaseAnalytics mFireBaseAnalytics;

    private static HashMap<String, DayMonth> punjabiMonthStart;
    private static HashMap<String, DayMonth> punjabiMonthEnd;

    @Override
    public void onCreate() {

        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("org.calendar.database")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        MobileAds.initialize(this, "ca-app-pub-2995010605730030~9551781504");
        Fabric.with(this, new Crashlytics());
        mFireBaseAnalytics = FirebaseAnalytics.getInstance(this);

        initializePunjabiMonths();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static FirebaseAnalytics getFireBaseAnalyticsInstance(){
        return mFireBaseAnalytics;
    }

    @Override
    public void onTerminate() {
        Realm.getDefaultInstance().close();
        super.onTerminate();
    }

    private void initializePunjabiMonths() {
        punjabiMonthStart = new HashMap<>();
        punjabiMonthEnd = new HashMap<>();

        DayMonth chetStart = new DayMonth(14, 3);
        DayMonth chetEnd = new DayMonth(13, 4);
        punjabiMonthStart.put("Chet", chetStart);
        punjabiMonthEnd.put("Chet", chetEnd);


        DayMonth vaisakhStart = new DayMonth(14, 4);
        DayMonth vaisakhEnd = new DayMonth(13, 5);
        punjabiMonthStart.put("Vaisakh", vaisakhStart);
        punjabiMonthEnd.put("Vaisakh", vaisakhEnd);


        DayMonth jethStart = new DayMonth(14, 5);
        DayMonth jethEnd = new DayMonth(14, 6);
        punjabiMonthStart.put("Jeth", jethStart);
        punjabiMonthEnd.put("Jeth", jethEnd);


        DayMonth HarhStart = new DayMonth(15, 6);
        DayMonth HarhEnd = new DayMonth(15, 7);
        punjabiMonthStart.put("Harh", HarhStart);
        punjabiMonthEnd.put("Harh", HarhEnd);

        DayMonth SawanStart = new DayMonth(16, 7);
        DayMonth SawanEnd = new DayMonth(16, 8);
        punjabiMonthStart.put("Sawan", SawanStart);
        punjabiMonthEnd.put("Sawan", SawanEnd);

        DayMonth BhadonStart = new DayMonth(17, 8);
        DayMonth BhadonEnd = new DayMonth(16, 9);
        punjabiMonthStart.put("Bhadon", BhadonStart);
        punjabiMonthEnd.put("Bhadon", BhadonEnd);

        DayMonth AssuStart = new DayMonth(17, 9);
        DayMonth AssuEnd = new DayMonth(16, 10);
        punjabiMonthStart.put("Assu", AssuStart);
        punjabiMonthEnd.put("Assu", AssuEnd);

        DayMonth KatakStart = new DayMonth(17, 10);
        DayMonth KatakEnd = new DayMonth(15, 11);
        punjabiMonthStart.put("Katak", KatakStart);
        punjabiMonthEnd.put("Katak", KatakEnd);

        DayMonth MagharStart = new DayMonth(16, 11);
        DayMonth MagharEnd = new DayMonth(15, 12);
        punjabiMonthStart.put("Maghar", MagharStart);
        punjabiMonthEnd.put("Maghar", MagharEnd);

//        DayMonth PohStart = new DayMonth(14, 12);
//        DayMonth PohEnd = new DayMonth(12, 1);
//        punjabiMonthStart.put("Poh", PohStart);
//        punjabiMonthEnd.put("Poh", PohEnd);

        DayMonth MaghStart = new DayMonth(14, 1);
        DayMonth MaghEnd = new DayMonth(12, 2);
        punjabiMonthStart.put("Magh", MaghStart);
        punjabiMonthEnd.put("Magh", MaghEnd);

        DayMonth PhagunStart = new DayMonth(13, 2);
        DayMonth PhagunEnd = new DayMonth(13, 3);
        punjabiMonthStart.put("Phagun", PhagunStart);
        punjabiMonthEnd.put("Phagun", PhagunEnd);


    }

    public static String getPunjabidate(Calendar calendar){
        DayMonth dayMonth = new DayMonth(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1);

        String month = "";
        int day = 0;
        if(dayMonth.getMonth() == 12){
            if (dayMonth.getDay() >= 16){
                month = "Poh";
                day = dayMonth.getDay() - 16 + 1;
            }
        }
        if(dayMonth.getMonth() == 1){
            if(dayMonth.getDay() <= 13) {
                month = "Poh";
                calendar.set(Calendar.MONTH, 11);
                int maxdays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                day = maxdays - 16 + dayMonth.getDay() + 1;
            }
        }

        Iterator<Map.Entry<String, DayMonth>> iter1 = punjabiMonthStart.entrySet().iterator();
        Iterator<Map.Entry<String, DayMonth>> iter2 = punjabiMonthEnd.entrySet().iterator();
        while(iter1.hasNext() || iter2.hasNext()) {
            Map.Entry<String, DayMonth> e1 = iter1.next();
            Map.Entry<String, DayMonth> e2 = iter2.next();
            if((dayMonth.compareTo(e1.getValue())==1 || dayMonth.compareTo(e1.getValue())==0) && (dayMonth.compareTo(e2.getValue())==-1 || dayMonth.compareTo(e2.getValue())==0)){
                month = e1.getKey();
                if(dayMonth.getMonth() == e1.getValue().getMonth()){
                    day = dayMonth.getDay() - e1.getValue().getDay() + 1;
                }else {
                    calendar.set(Calendar.MONTH, e1.getValue().getMonth()-1);
                    int maxdays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    day = maxdays - e1.getValue().getDay() + dayMonth.getDay() + 1;
                }
            }
        }

        return day + "-" + month + "-550";
    }
}
