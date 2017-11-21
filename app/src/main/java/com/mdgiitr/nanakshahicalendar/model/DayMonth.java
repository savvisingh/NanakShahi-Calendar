package com.mdgiitr.nanakshahicalendar.model;

import android.support.annotation.NonNull;

/**
 * Created by SavviSingh on 15/11/17.
 */

public class DayMonth implements Comparable<DayMonth>{
    private int day;
    private int month;

    public DayMonth(int day, int month) {
        this.day = day;
        this.month = month;
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

    @Override
    public int compareTo(@NonNull DayMonth dayMonth) {
        if(this.getMonth() < dayMonth.getMonth()){
            return -1;
        } else if(this.getMonth() > dayMonth.getMonth()){
            return 1;
        }else {
            if(this.getDay() < dayMonth.getDay())
                return -1;
            else if(this.getDay() > dayMonth.getDay())
                return 1;
            else return 0;
        }
    }
}

