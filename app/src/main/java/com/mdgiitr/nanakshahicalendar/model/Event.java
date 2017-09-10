package com.mdgiitr.nanakshahicalendar.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by SavviSingh on 26/01/17.
 */
@IgnoreExtraProperties
public class Event extends RealmObject{
    public int day, month, year;
    public String title;
    public  String description;
    public int event_type;
    public Date date;

    @PrimaryKey
    public int id;

    public Event(){}

    public Event(int day, int month, int year, String title, String description, int event_type) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.title = title;
        this.description = description;
        this.event_type = event_type;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        this.date = calendar.getTime();
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEvent_type() {
        return event_type;
    }

    public void setEvent_type(int event_type) {
        this.event_type = event_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
