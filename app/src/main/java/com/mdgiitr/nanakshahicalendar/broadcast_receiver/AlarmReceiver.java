package com.savvisingh.nanakshahicalendar.broadcast_receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.savvisingh.nanakshahicalendar.model.Event;

import apps.savvisingh.nanakshahicalendar.R;
import com.savvisingh.nanakshahicalendar.activities.HomeActivity;
import com.savvisingh.nanakshahicalendar.service.AlarmService;
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

        realm = Realm.getDefaultInstance();

        Log.d("AlarmReceiver", "onReceive");

        if(intent!=null){
            int id = intent.getIntExtra("event_id", 0);
            Event result = realm.where(Event.class).equalTo("id", id).findFirst();

            RealmResults<Event> results = realm.where(Event.class).equalTo("day", result.getDay()).equalTo("month", result.getMonth()).equalTo("year", result.getYear()).findAll();

            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.khanda_kesri_smallicon);

            if(results.size()>0){
                for (Event event: results){

                    PendingIntent myIntent = PendingIntent.getActivity(context, 0, new Intent(context, HomeActivity.class), 0);


                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.ic_khanda)
                                    .setTicker("Waheguru")
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(event.getDescription()))
                                    .setContentTitle(event.getTitle())
                                    .setContentText(event.getDescription())
                                    .setContentIntent(myIntent);

                    NotificationManager mNotificationManager = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(event.getId(),
                            mBuilder.build());
                }
            }
            AlarmService.setAlarm(context);
        }

        realm.close();



    }
}
