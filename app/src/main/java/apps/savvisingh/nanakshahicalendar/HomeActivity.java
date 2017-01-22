package apps.savvisingh.nanakshahicalendar;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView monthTextView;
    private BottomSheetDialog dialog;
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
                Calendar.LONG, Locale.ENGLISH) + " " + calendarView.getSelectedDateItem().getYear());

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
                monthTextView.setText(cal.getDisplayName(Calendar.MONTH,
                        Calendar.LONG, Locale.ENGLISH) + " " + year);

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
                if(year==2017 && month == 0 && day == 25){
                    List<CustomEvent> colorLst1 = new ArrayList<>();
                    colorLst1.add(new CustomEvent(android.R.color.holo_green_dark));
                    colorLst1.add(new CustomEvent(android.R.color.holo_blue_light));
                    colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                    return colorLst1;
                }
                if(year==2017 && month == 0 && day == 8){
                    List<CustomEvent> colorLst1 = new ArrayList<>();
                    colorLst1.add(new CustomEvent(android.R.color.holo_green_dark));
                    colorLst1.add(new CustomEvent(android.R.color.holo_blue_light));
                    colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                    return colorLst1;
                }
                if(year==2017 && month == 0 && day == 5){
                    List<CustomEvent> colorLst1 = new ArrayList<>();
                    colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                    return colorLst1;
                }
                return null;
            }
        });

        calendarView.setOnDateClickListener(new FlexibleCalendarView.OnDateClickListener() {
            @Override
            public void onDateClick(int year, int month, int day) {
                createDialog();
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

    private void createDialog() {
        if (dismissDialog()) {
            return;
        }

        List<SampleModel> list = new ArrayList<>();
        list.add(new SampleModel(R.string.share, R.mipmap.ic_launcher));
        list.add(new SampleModel(R.string.upload, R.mipmap.ic_launcher));
        list.add(new SampleModel(R.string.copy, R.mipmap.ic_launcher));
        list.add(new SampleModel(R.string.print, R.mipmap.ic_launcher));

        BottomSheetAdapter adapter = new BottomSheetAdapter(list);
        adapter.setOnItemClickListener(new BottomSheetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BottomSheetAdapter.ItemHolder item, int position) {
                //dismissDialog();
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
