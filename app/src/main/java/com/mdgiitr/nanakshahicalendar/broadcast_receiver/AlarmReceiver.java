package com.mdgiitr.nanakshahicalendar.broadcast_receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mdgiitr.nanakshahicalendar.model.Event;

import apps.savvisingh.nanakshahicalendar.R;
import com.mdgiitr.nanakshahicalendar.activities.HomeActivity;
import com.mdgiitr.nanakshahicalendar.service.AlarmService;
import com.mdgiitr.nanakshahicalendar.util.Logr;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {}

    private Realm realm;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent!=null){

            try {
                realm = Realm.getDefaultInstance();
            } catch (IllegalStateException fuckYouTooAndroid) {
                Realm.init(context.getApplicationContext());
                realm = Realm.getDefaultInstance();
            }

            Log.d("AlarmReceiver", "onReceive");

            try{
                Calendar cal = Calendar.getInstance();
                RealmResults<Event> results = realm.where(Event.class).equalTo("day", cal.get(Calendar.DAY_OF_MONTH)).equalTo("month", cal.get(Calendar.MONTH)).equalTo("year", cal.get(Calendar.YEAR)).findAll();

                if(results.size()>0){
                    for (Event event : results){

                        PendingIntent myIntent = PendingIntent.getActivity(context, 0, new Intent(context, HomeActivity.class), 0);

                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context, "default")
                                        .setSmallIcon(R.drawable.ic_khanda)
                                        .setTicker("Waheguru")
                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(event.getDescription()))
                                        .setContentTitle(event.getTitle())
                                        .setContentText(event.getDescription())
                                        .setContentIntent(myIntent);


                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                        {
                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
                            NotificationChannel notificationChannel = new NotificationChannel("default", "Reminders", importance);
                            notificationChannel.setDescription("Get Reminders for Sikhi Events");
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.GREEN);
                            notificationChannel.enableVibration(true);
                            assert mNotificationManager != null;
                            mBuilder.setChannelId("default");
                            mNotificationManager.createNotificationChannel(notificationChannel);
                        }
                        assert mNotificationManager != null;
                        mNotificationManager.notify(event.getId(), mBuilder.build());
                    }
                }
                AlarmService.setAlarm(context);
                if(realm!=null && !realm.isClosed())
                    realm.close();
            }catch(Exception e){
                Logr.e(e);
                if(realm!=null && !realm.isClosed())
                    realm.close();
            }

        }

    }
}
