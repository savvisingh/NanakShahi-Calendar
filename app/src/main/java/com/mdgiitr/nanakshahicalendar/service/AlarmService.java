package com.savvisingh.nanakshahicalendar.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.savvisingh.nanakshahicalendar.broadcast_receiver.AlarmReceiver;
import com.savvisingh.nanakshahicalendar.model.Event;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

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
        context.startService(intent);
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
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        realm = Realm.getDefaultInstance();
        RealmResults<Event> results = realm.where(Event.class).greaterThan("day", day).greaterThanOrEqualTo("month", month).greaterThanOrEqualTo("year", year).findAllSorted("id");

        Log.d("Events", results.size()+" ");

        if(results.size()>0){

            Event event = results.get(0);


            Calendar calobj = Calendar.getInstance();

            calobj.set(Calendar.YEAR, event.getYear());
            calobj.set(Calendar.MONTH, event.getMonth());
            calobj.set(Calendar.DAY_OF_MONTH, event.getDay());
            calobj.set(Calendar.HOUR_OF_DAY, 00);
            calobj.set(Calendar.MINUTE, 01);

            Log.d("Alarm time", calobj.getTime().toString() + " ");
            Log.d("Alarm time", calobj.getTimeInMillis() + " ");



            alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

            intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("event_id", event.getId());
            pendingIntent = PendingIntent.getBroadcast(this, event.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.set(AlarmManager.RTC_WAKEUP, calobj.getTimeInMillis(), pendingIntent);
        }

        realm.close();


    }


}
