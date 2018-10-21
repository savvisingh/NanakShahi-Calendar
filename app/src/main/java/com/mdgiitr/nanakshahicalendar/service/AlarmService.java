package com.mdgiitr.nanakshahicalendar.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.mdgiitr.nanakshahicalendar.broadcast_receiver.AlarmReceiver;
import com.mdgiitr.nanakshahicalendar.model.Event;
import com.mdgiitr.nanakshahicalendar.util.Logr;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AlarmService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_ALARM = "apps.savvisingh.nanakshahicalendar.service.action.alarm";

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent intent;
    private Realm realm;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1,new Notification());
    }
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void setAlarm(Context context) {
        Intent intent = new Intent(context, AlarmService.class);
        intent.setAction(ACTION_ALARM);
        ContextCompat.startForegroundService(context, intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Alarm Service", "on Handle Intent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ALARM.equals(action)) {
                handleActionAlarm();
            }
        }
    }

    /**
     * Handle action Alarm in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAlarm() {


        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();

        try{
            realm = Realm.getDefaultInstance();

            RealmResults<Event> results = realm.where(Event.class).greaterThan("date", currentDate).findAllSorted("date", Sort.ASCENDING);

            if(results.size()>0){

                Event event = results.get(0);
                Calendar calobj = Calendar.getInstance();

                calobj.set(Calendar.YEAR, event.getYear());
                calobj.set(Calendar.MONTH, event.getMonth());
                calobj.set(Calendar.DAY_OF_MONTH, event.getDay());
                calobj.set(Calendar.HOUR_OF_DAY, 0);
                calobj.set(Calendar.MINUTE, 1);


                alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

                intent = new Intent(this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(this, event.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

//                if (Build.VERSION.SDK_INT >= 23) {
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calobj.getTimeInMillis(), pendingIntent);
//                } else if(Build.VERSION.SDK_INT >= 19){
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calobj.getTimeInMillis(), pendingIntent);
//                } else{
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, calobj.getTimeInMillis(), pendingIntent);
//                }

                alarmManager.set(AlarmManager.RTC, calobj.getTimeInMillis(), pendingIntent);

            }

            if(realm!=null && !realm.isClosed())
                realm.close();
        }catch(Exception e){
            Logr.e(e);
            if(realm!=null && !realm.isClosed())
                realm.close();
        }





    }


}
