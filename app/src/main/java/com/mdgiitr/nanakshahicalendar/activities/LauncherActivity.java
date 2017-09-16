package com.mdgiitr.nanakshahicalendar.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import apps.savvisingh.nanakshahicalendar.R;

import com.google.firebase.messaging.FirebaseMessaging;
import com.mdgiitr.nanakshahicalendar.data.SharedPrefHelper;
import com.mdgiitr.nanakshahicalendar.model.Event;

import java.util.Calendar;

import io.realm.Realm;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LauncherActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launcher);
        try {
            realm = Realm.getDefaultInstance();
        } catch (IllegalStateException e) {
            Crashlytics.logException(e);
            Realm.init(getApplicationContext());
            realm = Realm.getDefaultInstance();
        }

        final SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(getApplicationContext());

        Log.d("checks", sharedPrefHelper.getDatabaseDownloaded() + "");

        if(sharedPrefHelper.getDatabaseDownloaded()){
            Intent intent = new Intent(LauncherActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }else {
            FirebaseMessaging.getInstance().subscribeToTopic("updates");
            FirebaseMessaging.getInstance().subscribeToTopic("UpdateDatabase");
            FirebaseMessaging.getInstance().subscribeToTopic("notify");
            DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

            mFirebaseDatabase.child("Events").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (final DataSnapshot item : dataSnapshot.getChildren()) {

                            final Event event = new Event();
                            event.setId(((Long) item.child("id").getValue()).intValue());
                            event.setDay(((Long) item.child("day").getValue()).intValue());
                            event.setMonth(((Long) item.child("month").getValue()).intValue() - 1 );
                            event.setYear(((Long) item.child("year").getValue()).intValue());
                            event.setTitle((String) item.child("title").getValue());
                            event.setDescription((String) item.child("description").getValue());
                            event.setEvent_type(((Long) item.child("event_type").getValue()).intValue());
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.DAY_OF_MONTH, event.getDay());
                            cal.set(Calendar.MONTH, event.getMonth());
                            cal.set(Calendar.YEAR, event.getYear());
                            cal.set(Calendar.HOUR_OF_DAY, 0);
                            cal.set(Calendar.MINUTE, 0);
                            event.setDate(cal.getTime());

                            //Log.d("Event", item.toString());
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(event);
                                }
                            });

                        }

                        sharedPrefHelper.setDataBaseDownloaded(true);
                        Log.d("checks", sharedPrefHelper.getDatabaseDownloaded() + "");
                        Intent intent = new Intent(LauncherActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    sharedPrefHelper.setDataBaseDownloaded(false);
                    Crashlytics.logException(databaseError.toException());
                }
            });

        }

    }


}
