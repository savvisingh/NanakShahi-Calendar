package com.mdgiitr.nanakshahicalendar.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mdgiitr.nanakshahicalendar.data.SharedPrefHelper;
import com.mdgiitr.nanakshahicalendar.model.CalenderEvent;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;


public class UpdateDataBaseService extends IntentService {

    public static final String UPDATE_DATABASE = "Update data";
    public static final String DATABASE_VERSION = "database version";

    private Realm realm;

    public UpdateDataBaseService() {
        super("UpdateDataBaseService");
    }


    // TODO: Customize helper method
    public static void startUpdateDataService(Context context, String version) {
        Intent intent = new Intent(context, UpdateDataBaseService.class);
        intent.setAction(UPDATE_DATABASE);
        intent.putExtra(DATABASE_VERSION, version);
        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if(action.equalsIgnoreCase(UPDATE_DATABASE)){
                String version = intent.getStringExtra(DATABASE_VERSION);
                updateData(version);
            }
        }
    }

    private void updateData(final String version){

        final SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(getApplicationContext());

        Log.d("UpdateDataBaseService", sharedPrefHelper.getDataBaseVersion()+" jkjkj");

        if(sharedPrefHelper.getDatabaseDownloaded() && !sharedPrefHelper.getDataBaseVersion().equalsIgnoreCase(version)){

            DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

            final TaskCompletionSource<DataSnapshot> tcs = new TaskCompletionSource<>();
            mFirebaseDatabase.child("Events").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("UpdateDataBaseService", "OnDataChange Called");
                    tcs.setResult(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    tcs.setException(databaseError.toException());
                }
            });


            Task<DataSnapshot> t = tcs.getTask();

            try {
                Tasks.await(t);
            } catch (ExecutionException | InterruptedException e) {
                t = Tasks.forException(e);
            }

            if(t.isSuccessful()) {
                DataSnapshot dataSnapshot = t.getResult();
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot item : dataSnapshot.getChildren()) {

                        try {
                            realm = Realm.getDefaultInstance();
                        } catch (IllegalStateException e) {
                            Realm.init(getApplicationContext());
                            realm = Realm.getDefaultInstance();
                        }

                        final CalenderEvent calenderEvent = new CalenderEvent();
                        calenderEvent.setId(((Long) item.child("id").getValue()).intValue());
                        calenderEvent.setDay(((Long) item.child("day").getValue()).intValue() + 1);
                        calenderEvent.setMonth(((Long) item.child("month").getValue()).intValue() - 1);
                        calenderEvent.setYear(((Long) item.child("year").getValue()).intValue());
                        //calenderEvent.setTitle((String) item.child("title").getValue());
                        calenderEvent.setTitle("Test Test");
                        calenderEvent.setDescription((String) item.child("description").getValue());
                        calenderEvent.setEvent_type(((Long) item.child("event_type").getValue()).intValue());
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.DAY_OF_MONTH, calenderEvent.getDay());
                        cal.set(Calendar.MONTH, calenderEvent.getMonth());
                        cal.set(Calendar.YEAR, calenderEvent.getYear());
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        calenderEvent.setDate(cal.getTime());

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(calenderEvent);
                            }
                        });

                    }

                    sharedPrefHelper.setDataBaseVersion(version);
                    Log.d("checks", sharedPrefHelper.getDataBaseVersion());
                }
            }else {
                t.getException().printStackTrace();
            }
        }

    }

}
