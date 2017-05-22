package apps.savvisingh.nanakshahicalendar.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import apps.savvisingh.nanakshahicalendar.R;
import apps.savvisingh.nanakshahicalendar.model.Event;
import io.realm.Realm;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LauncherActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;



    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launcher);


        // Create the Realm instance
        realm = Realm.getDefaultInstance();

        if(realm.where(Event.class).count()>0){
            Intent intent = new Intent(LauncherActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }else {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

            mFirebaseDatabase.child("Events").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (final DataSnapshot item : dataSnapshot.getChildren()) {

                            Log.d("Event", item.toString());
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Event event = realm.createObject(Event.class, item.child("id").getValue());
                                    event.setDay(((Long) item.child("day").getValue()).intValue());
                                    event.setMonth(((Long) item.child("month").getValue()).intValue() - 1 );
                                    event.setYear(((Long) item.child("year").getValue()).intValue());
                                    event.setTitle((String) item.child("title").getValue());
                                    event.setDescription((String) item.child("description").getValue());
                                    event.setEvent_type(((Long) item.child("event_type").getValue()).intValue());
                                }
                            });

                        }

                        Intent intent = new Intent(LauncherActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }


}
