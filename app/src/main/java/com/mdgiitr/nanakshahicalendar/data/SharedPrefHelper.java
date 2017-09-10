package com.mdgiitr.nanakshahicalendar.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mdgiitr.nanakshahicalendar.util.AppConstants;

/**
 * Created by SavviSingh on 25/08/17.
 */

public class SharedPrefHelper {

    SharedPreferences sharedPreferences;


    public SharedPrefHelper(Context mContext){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public boolean getDatabaseDownloaded(){
        return sharedPreferences.getBoolean(AppConstants.KEY_DATABASE_SAVED, false);
    }

    public void setDataBaseDownloaded(boolean flag){
        sharedPreferences.edit().putBoolean(AppConstants.KEY_DATABASE_SAVED, flag).apply();
    }
}
