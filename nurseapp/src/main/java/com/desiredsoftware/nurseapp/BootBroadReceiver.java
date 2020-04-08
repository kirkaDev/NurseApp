package com.desiredsoftware.nurseapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadReceiver extends BroadcastReceiver {

    final String My_Log_Tag = "My_TAG";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(My_Log_Tag, "onRecieve " + intent.getAction());
        Intent serviceIntent = new Intent(context, AwakeNurseService.class);
        context.startService(serviceIntent);
    }
}
