package com.mdgiitr.nanakshahicalendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mdgiitr.nanakshahicalendar.model.Event;
import com.mdgiitr.nanakshahicalendar.util.AppConstants;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import apps.savvisingh.nanakshahicalendar.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class EventActivity extends AppCompatActivity {


    private Realm realm;

    @BindView(R.id.event_title)
    TextView eventtitle;

    @BindView(R.id.event_description)
    TextView eventDescription;

    private String eventId = null;

    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        realm = Realm.getDefaultInstance();

        myCalendar = Calendar.getInstance();

        if(getIntent().getStringExtra("event_id") != null)
            eventId = getIntent().getStringExtra("event_id");
        else this.finish();

        Event event = realm.where(Event.class).equalTo("id", Integer.parseInt(eventId)).findFirst();

        if(event != null){
            eventtitle.setText(event.getTitle());

            if(event.getDescription() != null)
                eventDescription.setText(event.getDescription());
            else
                eventDescription.setVisibility(View.GONE);

            myCalendar.set(Calendar.YEAR, event.getYear());
            myCalendar.set(Calendar.MONTH, event.getMonth());
            myCalendar.set(Calendar.DAY_OF_MONTH, event.getDay());

            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            String formattedDate = df.format(myCalendar.getTime());
            getSupportActionBar().setTitle(formattedDate);
        }else {
            Toast.makeText(getApplicationContext(), "Oops Something went wrong", Toast.LENGTH_SHORT);
            onBackPressed();
        }

    }

    @OnClick(R.id.event_edit)
    public void editEvent(){
        Intent intent = new Intent(EventActivity.this, AddEditEvent.class);
        intent.putExtra("event_id", eventId);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
