package com.mdgiitr.nanakshahicalendar.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mdgiitr.nanakshahicalendar.service.AlarmService;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            AlarmService.setAlarm(context);

        }
    }
}
