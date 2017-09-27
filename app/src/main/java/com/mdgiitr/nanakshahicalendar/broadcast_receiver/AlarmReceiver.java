package com.mdgiitr.nanakshahicalendar.broadcast_receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mdgiitr.nanakshahicalendar.model.CalenderEvent;

import apps.savvisingh.nanakshahicalendar.R;
import com.mdgiitr.nanakshahicalendar.activities.HomeActivity;
import com.mdgiitr.nanakshahicalendar.service.AlarmService;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    private Realm realm;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        try {
            realm = Realm.getDefaultInstance();
        } catch (IllegalStateException fuckYouTooAndroid) {
            Realm.init(context.getApplicationContext());
            realm = Realm.getDefaultInstance();
        }

        Log.d("AlarmReceiver", "onReceive");

        if(intent!=null){

            Calendar cal = Calendar.getInstance();
            RealmResults<CalenderEvent> results = realm.where(CalenderEvent.class).equalTo("day", cal.get(Calendar.DAY_OF_MONTH)).equalTo("month", cal.get(Calendar.MONTH)).equalTo("year", cal.get(Calendar.YEAR)).findAll();

            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.khanda_kesri_smallicon);

            if(results.size()>0){
                for (CalenderEvent calenderEvent : results){

                    PendingIntent myIntent = PendingIntent.getActivity(context, 0, new Intent(context, HomeActivity.class), 0);

                    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.ic_khanda)
                                    .setTicker("Waheguru")
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(calenderEvent.getDescription()))
                                    .setContentTitle(calenderEvent.getTitle())
                                    .setContentText(calenderEvent.getDescription())
                                    .setSound(defaultSoundUri)
                                    .setContentIntent(myIntent);

                    NotificationManager mNotificationManager = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(calenderEvent.getId(),
                            mBuilder.build());
                }
            }
            AlarmService.setAlarm(context);
        }

        realm.close();



    }
}
