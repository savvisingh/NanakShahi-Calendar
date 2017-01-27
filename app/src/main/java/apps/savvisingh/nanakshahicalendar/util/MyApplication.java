package apps.savvisingh.nanakshahicalendar.util;

import android.app.Application;

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
}
