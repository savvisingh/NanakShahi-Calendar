package com.mdgiitr.nanakshahicalendar.service;

import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mdgiitr.nanakshahicalendar.data.SharedPrefHelper;
import com.mdgiitr.nanakshahicalendar.model.Event;

import java.util.Calendar;

import io.realm.Realm;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";
    public static final String UPDATE_DATABASE_TOPIC = "/topics/UpdateDatabase";
    public static final String DATABASE_VERSION = "version";

    private Realm realm;

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        String from = remoteMessage.getFrom();
        if(from.equalsIgnoreCase(UPDATE_DATABASE_TOPIC)){
            if(!remoteMessage.getData().isEmpty() && remoteMessage.getData().containsKey(DATABASE_VERSION)){
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                //UpdateDataBaseService.startUpdateDataService(getApplicationContext(), remoteMessage.getData().get(DATABASE_VERSION));
                updateData(remoteMessage.getData().get(DATABASE_VERSION));
                return;
            }
        }


//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }


    private void updateData(final String version){

        final SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(getApplicationContext());

        Log.d("UpdateDataBaseService", sharedPrefHelper.getDataBaseVersion()+" jkjkj");

        if(sharedPrefHelper.getDatabaseDownloaded() && !sharedPrefHelper.getDataBaseVersion().equalsIgnoreCase(version)){
            FirebaseApp.initializeApp(getApplicationContext());

            DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

            mFirebaseDatabase.child("Events").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (final DataSnapshot item : dataSnapshot.getChildren()) {

                            try {
                                realm = Realm.getDefaultInstance();
                            } catch (IllegalStateException e) {
                                Realm.init(getApplicationContext());
                                realm = Realm.getDefaultInstance();
                            }

                            final Event event = new Event();
                            event.setId(((Long) item.child("id").getValue()).intValue());
                            event.setDay(((Long) item.child("day").getValue()).intValue() + 1);
                            event.setMonth(((Long) item.child("month").getValue()).intValue() - 1 );
                            event.setYear(((Long) item.child("year").getValue()).intValue());
                            //event.setTitle((String) item.child("title").getValue());
                            event.setTitle("Test Test");
                            event.setDescription((String) item.child("description").getValue());
                            event.setEvent_type(((Long) item.child("event_type").getValue()).intValue());
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.DAY_OF_MONTH, event.getDay());
                            cal.set(Calendar.MONTH, event.getMonth());
                            cal.set(Calendar.YEAR, event.getYear());
                            cal.set(Calendar.HOUR_OF_DAY, 0);
                            cal.set(Calendar.MINUTE, 0);
                            event.setDate(cal.getTime());

                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(event);
                                }
                            });

                        }

                        sharedPrefHelper.setDataBaseVersion(version);
                        Log.d("checks", sharedPrefHelper.getDataBaseVersion());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
