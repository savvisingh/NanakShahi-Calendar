package apps.savvisingh.nanakshahicalendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import apps.savvisingh.nanakshahicalendar.adapter.BottomSheetAdapter;
import apps.savvisingh.nanakshahicalendar.R;
import apps.savvisingh.nanakshahicalendar.calendarview.CustomEvent;
import io.realm.Realm;
import io.realm.RealmResults;

import static apps.savvisingh.nanakshahicalendar.util.AppConstants.GURUPURAB;
import static apps.savvisingh.nanakshahicalendar.util.AppConstants.HISTORICAL_DAYS;
import static apps.savvisingh.nanakshahicalendar.util.AppConstants.MASYA;
import static apps.savvisingh.nanakshahicalendar.util.AppConstants.PURANMASHI;
import static apps.savvisingh.nanakshahicalendar.util.AppConstants.SAGRANDH;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView monthTextView;
    private BottomSheetDialog dialog;


    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        InitNavigationDrawer();
        InitCalendarView();

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(cal.getTime());

        getSupportActionBar().setTitle(formattedDate);

        realm = Realm.getDefaultInstance();

        Log.d("events", String.valueOf(realm.where(apps.savvisingh.nanakshahicalendar.classes.Event.class).count()));


    }


    private void InitNavigationDrawer(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    private void InitCalendarView(){
        final FlexibleCalendarView calendarView = (FlexibleCalendarView)findViewById(R.id.calendar_view);
        calendarView.setStartDayOfTheWeek(Calendar.MONDAY);

        ImageView leftArrow = (ImageView)findViewById(R.id.left_arrow);
        ImageView rightArrow = (ImageView)findViewById(R.id.right_arrow);

        monthTextView = (TextView)findViewById(R.id.month_text_view);

        Calendar cal = Calendar.getInstance();
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
                    cellView.setTextSize(14);
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

                RealmResults<apps.savvisingh.nanakshahicalendar.classes.Event> results = realm.where(apps.savvisingh.nanakshahicalendar.classes.Event.class).equalTo("day", day)
                        .equalTo("month", month+1).equalTo("year", year).findAll();

                if(results.size()>0){
                    Log.d("Events", results.size() +" ");
                    for(apps.savvisingh.nanakshahicalendar.classes.Event event:results){
                        switch (event.getEvent_type()) {
                            case MASYA:
                                colorLst.add(new CustomEvent(android.R.color.black));
                                break;
                            case SAGRANDH:
                                colorLst.add(new CustomEvent(android.R.color.holo_red_dark));
                                break;
                            case GURUPURAB:
                                colorLst.add(new CustomEvent(android.R.color.holo_red_dark));
                                break;
                            case PURANMASHI:
                                colorLst.add(new CustomEvent(android.R.color.holo_blue_dark));
                                break;
                            case HISTORICAL_DAYS:
                                colorLst.add(new CustomEvent(android.R.color.holo_blue_dark));
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

                String dateStr = "";
                if(day>9){
                    dateStr+=day;
                }else {
                    dateStr+= "0" + day;
                }

                if(month+1 >9){
                    dateStr += "/" + month+1;
                }else {
                    dateStr += "/" + "0" + month+1;
                }

                dateStr+="/"+year;

                SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
                Date dateObj = null;
                try {
                    dateObj = curFormater.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat postFormater = new SimpleDateFormat("dd-MMM-yyyy");

                String newDateStr = postFormater.format(dateObj);
                getSupportActionBar().setTitle(newDateStr);

                RealmResults<apps.savvisingh.nanakshahicalendar.classes.Event> realmResults = realm.where(apps.savvisingh.nanakshahicalendar.classes.Event.class).equalTo("day", day)
                        .equalTo("month", month+1).equalTo("year", year).findAll();

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

    private void createDialog(final RealmResults<apps.savvisingh.nanakshahicalendar.classes.Event> results) {
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
                startActivity(intent);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
