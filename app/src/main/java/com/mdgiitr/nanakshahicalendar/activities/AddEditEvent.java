package com.mdgiitr.nanakshahicalendar.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.mdgiitr.nanakshahicalendar.model.Event;
import com.mdgiitr.nanakshahicalendar.util.AppConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import apps.savvisingh.nanakshahicalendar.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.realm.Realm;
import io.realm.RealmResults;

public class AddEditEvent extends AppCompatActivity {

    @BindView(R.id.event_title)
    EditText eventTitleEditText;

    @BindView(R.id.event_description)
    EditText eventDescriptionEditText;

    @BindView(R.id.event_title_layout)
    TextInputLayout eventTitleLayout;

    @BindView(R.id.event_date)
    TextView eventDateTextView;

    @BindView(R.id.clear_delete_action)
    AppCompatButton action;


    private int eventId;

    private Realm realm;

    private Event event = null;

    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_event);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        realm = Realm.getDefaultInstance();
        myCalendar = Calendar.getInstance();

        Intent intent = getIntent();
        try{
            if(intent.getExtras() != null){
                if(intent.getStringExtra("event_id") != null){
                    eventId = Integer.parseInt(intent.getStringExtra("event_id"));
                    RealmResults<Event> realmResults = realm.where(com.mdgiitr.nanakshahicalendar.model.Event.class).equalTo("id", eventId).findAll();
                    if(realmResults.size()>0){
                        event = realmResults.get(0);
                    }
                    if(event != null){
                        myCalendar.set(Calendar.YEAR, event.getYear());
                        myCalendar.set(Calendar.MONTH, event.getMonth());
                        myCalendar.set(Calendar.DAY_OF_MONTH, event.getDay());
                        eventTitleEditText.setText(event.getTitle());
                        if(event.getDescription() != null){
                            eventDescriptionEditText.setText(event.getDescription());
                        }
                        action.setText("Delete");
                    }
                }

                if(intent.getLongExtra(AppConstants.TIME_IN_MILLI_SEC, 0) != 0){
                    myCalendar.setTimeInMillis(intent.getLongExtra(AppConstants.TIME_IN_MILLI_SEC, 0));
                }

            }

        }catch (Exception e){
            Log.d("AddEditEvent", e.getMessage() + "-" + e.getLocalizedMessage());
        }

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String formattedDate = df.format(myCalendar.getTime());
        eventDateTextView.setText(formattedDate);

    }


    @OnClick(R.id.clear_delete_action)
    public void clearData(){
        String actionText = action.getText().toString();
        if(actionText.equalsIgnoreCase("Clear")){
            eventTitleEditText.setText("");
            eventDescriptionEditText.setText("");
        }else if(actionText.equalsIgnoreCase("Delete")){
            try{
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        if(event!=null)
                            event.deleteFromRealm();
                        onBackPressed();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                onBackPressed();
            }
        }

    }


    @OnClick(R.id.save_action)
    public void saveEvent(){

        final String eventTitle = eventTitleEditText.getText().toString();
        if(TextUtils.isEmpty(eventTitle)){
            eventTitleLayout.setError("Title cannot be empty");
            return;
        }

        if(event==null){
            event = new Event();
            event.setEvent_type(AppConstants.GOVERNMENT_HOLIDAY);
            int maxId = realm.where(Event.class).max("id").intValue();
            int nextId = maxId + 1;
            if(maxId < 5000){
                nextId = 5000;
            }
            event.setId(nextId);
        }

        try{
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    event.setTitle(eventTitle);
                    event.setDay(myCalendar.get(Calendar.DAY_OF_MONTH));
                    event.setMonth(myCalendar.get(Calendar.MONTH));
                    event.setYear(myCalendar.get(Calendar.YEAR));
                    myCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    myCalendar.set(Calendar.MINUTE, 0);
                    event.setDate(myCalendar.getTime());

                    String eventDescription = eventDescriptionEditText.getText().toString();
                    if(!TextUtils.isEmpty(eventDescription))
                        event.setDescription(eventDescription);

                    realm.copyToRealmOrUpdate(event);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


        onBackPressed();
    }

    @OnTextChanged(R.id.event_title)
    public void onTextChanged(){
        eventTitleLayout.setError(null);
    }

    @OnClick(R.id.changeDate)
    public void openDatePicker(){
        new DatePickerDialog(AddEditEvent.this, datePickerListener,myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            String formattedDate = df.format(myCalendar.getTime());

            eventDateTextView.setText(formattedDate);
        }

    };
}
