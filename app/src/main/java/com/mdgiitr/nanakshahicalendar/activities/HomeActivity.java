package com.mdgiitr.nanakshahicalendar.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.mdgiitr.nanakshahicalendar.adapter.BottomSheetAdapter;
import com.mdgiitr.nanakshahicalendar.calendarview.CustomEvent;
import com.mdgiitr.nanakshahicalendar.service.AlarmService;
import com.mdgiitr.nanakshahicalendar.util.AppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import apps.savvisingh.nanakshahicalendar.R;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity {

    private TextView monthTextView;
    private BottomSheetDialog dialog;

    private AdView mAdView;


    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        InitNavigationDrawer();
        InitCalendarView();

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(cal.getTime());

        getSupportActionBar().setTitle(formattedDate);

        realm = Realm.getDefaultInstance();

        Log.d("events", String.valueOf(realm.where(com.mdgiitr.nanakshahicalendar.model.Event.class).count()));

        AlarmService.setAlarm(this);

    }


    private void InitNavigationDrawer(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    private void InitCalendarView(){
        final FlexibleCalendarView calendarView = (FlexibleCalendarView)findViewById(R.id.calendar_view);
        calendarView.setStartDayOfTheWeek(Calendar.MONDAY);

        ImageView leftArrow = (ImageView)findViewById(R.id.left_arrow);
        ImageView rightArrow = (ImageView)findViewById(R.id.right_arrow);

        monthTextView = (TextView)findViewById(R.id.month_text_view);

        final Calendar cal = Calendar.getInstance();
        cal.set(calendarView.getSelectedDateItem().getYear(), calendarView.getSelectedDateItem().getMonth(), 1);
        monthTextView.setText(cal.getDisplayName(Calendar.MONTH,
                Calendar.LONG, Locale.ENGLISH) );


        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.moveToPreviousMonth();
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.moveToNextMonth();
            }
        });
        calendarView.setOnMonthChangeListener(new FlexibleCalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month, @FlexibleCalendarView.Direction int direction) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, 1);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(cal.getTime());

                getSupportActionBar().setTitle(formattedDate);


                monthTextView.setText(cal.getDisplayName(Calendar.MONTH,
                        Calendar.LONG, Locale.ENGLISH));

            }
        });
        calendarView.setShowDatesOutsideMonth(false);

        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            @Override
            public BaseCellView getCellView(int position, View convertView, ViewGroup parent, int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
                    cellView = (BaseCellView) inflater.inflate(R.layout.date_cell_view, null);
                }
                if (cellType == BaseCellView.TODAY) {
                    cellView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                    cellView.setTextSize(16);
                } else {
                    cellView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    cellView.setTextSize(16);
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, View convertView, ViewGroup parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
                    cellView = (BaseCellView) inflater.inflate(R.layout.week_cell_view, null);
                    cellView.setTextSize(15);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                return null;
            }
        });

        calendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
            @Override
            public List<? extends Event> getEventsForTheDay(int year, int month, int day) {

                List<CustomEvent> colorLst = new ArrayList<>();

                RealmResults<com.mdgiitr.nanakshahicalendar.model.Event> results = realm.where(com.mdgiitr.nanakshahicalendar.model.Event.class).equalTo("day", day)
                        .equalTo("month", month).equalTo("year", year).findAll();

                if(results.size()>0){
                    Log.d("Events", results.size() +" ");
                    for(com.mdgiitr.nanakshahicalendar.model.Event event:results){
                        switch (event.getEvent_type()) {
                            case AppConstants.MASYA:
                                colorLst.add(new CustomEvent(android.R.color.black));
                                break;
                            case AppConstants.SAGRANDH:
                                colorLst.add(new CustomEvent(android.R.color.holo_purple));
                                break;
                            case AppConstants.GURUPURAB:
                                colorLst.add(new CustomEvent(android.R.color.holo_red_dark));
                                break;
                            case AppConstants.PURANMASHI:
                                colorLst.add(new CustomEvent(android.R.color.holo_orange_light));
                                break;
                            case AppConstants.HISTORICAL_DAYS:
                                colorLst.add(new CustomEvent(android.R.color.holo_blue_dark));
                                break;
                            case AppConstants.GOVERNMENT_HOLIDAY:
                                colorLst.add(new CustomEvent(android.R.color.holo_orange_dark));
                                break;
                        }
                    }
                }


                return colorLst;
            }


        });

        calendarView.setOnDateClickListener(new FlexibleCalendarView.OnDateClickListener() {
            @Override
            public void onDateClick(int year, int month, int day) {


                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(cal.getTime());

                getSupportActionBar().setTitle(formattedDate);

                RealmResults<com.mdgiitr.nanakshahicalendar.model.Event> realmResults = realm.where(com.mdgiitr.nanakshahicalendar.model.Event.class).equalTo("day", day)
                        .equalTo("month", month).equalTo("year", year).findAll();

                if(realmResults.size()>0){
                    createDialog(realmResults);
                }

            }
        });
    }


    private boolean dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            return true;
        }

        return false;
    }

    private void createDialog(final RealmResults<com.mdgiitr.nanakshahicalendar.model.Event> results) {
        if (dismissDialog()) {
            return;
        }


        BottomSheetAdapter adapter = new BottomSheetAdapter(results);
        adapter.setOnItemClickListener(new BottomSheetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BottomSheetAdapter.ItemHolder item, int position) {
                dismissDialog();
                Intent intent = new Intent(HomeActivity.this, EventActivity.class);
                intent.putExtra("event_id", results.get(position).getId());
                //startActivity(intent);
            }
        });

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        ComponentName componentName = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about_us) {

            Intent intent = new Intent(HomeActivity.this, AboutDeveloper.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}