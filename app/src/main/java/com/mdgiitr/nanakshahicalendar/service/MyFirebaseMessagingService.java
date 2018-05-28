package com.mdgiitr.nanakshahicalendar.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mdgiitr.nanakshahicalendar.activities.HomeActivity;
import com.mdgiitr.nanakshahicalendar.data.SharedPrefHelper;
import com.mdgiitr.nanakshahicalendar.model.Event;

import java.util.Calendar;

import apps.savvisingh.nanakshahicalendar.R;
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
                 updateData(remoteMessage.getData().get(DATABASE_VERSION));
                return;
            }
        }

        sendNotification(remoteMessage);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }


    private void updateData(final String version){


        final SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(getApplicationContext());

        if(sharedPrefHelper.getDatabaseDownloaded() && !sharedPrefHelper.getDataBaseVersion().equalsIgnoreCase(version)){

            final TaskCompletionSource<DataSnapshot> tcs = new TaskCompletionSource<>();

            FirebaseApp.initializeApp(getApplicationContext());

            DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

            mFirebaseDatabase.child("Events").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    tcs.setResult(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    tcs.setException(databaseError.toException());
                }
            });

            Task<DataSnapshot> task = tcs.getTask();

            try{
                Tasks.await(task);
            }catch (Exception e){
                task = Tasks.forException(e);
            }

            if(task.isSuccessful()){
                DataSnapshot dataSnapshot = task.getResult();
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
                        event.setDay(((Long) item.child("day").getValue()).intValue());
                        event.setMonth(((Long) item.child("month").getValue()).intValue()-1);
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
            }else {
                Crashlytics.logException(task.getException());
            }

        }

    }


    private void sendNotification(RemoteMessage remoteMessage) {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_khanda)
                        .setTicker("Waheguru")
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(pendingIntent)
                        .setSound(defaultSoundUri);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert mNotificationManager != null;
        mNotificationManager.notify(12321, mBuilder.build());
    }
}
