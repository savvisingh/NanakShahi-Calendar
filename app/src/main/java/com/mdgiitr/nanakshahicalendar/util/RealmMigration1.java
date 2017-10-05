package com.mdgiitr.nanakshahicalendar.util;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by SavviSingh on 05/10/17.
 */

public class RealmMigration1 implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if (oldVersion == 1) {

        }
    }
}
