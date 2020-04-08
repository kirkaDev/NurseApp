package com.desiredsoftware.nurseapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

public class AwakeNurseService extends IntentService {
    public AwakeNurseService() {
        super("My awake nurse service");
    }

    String My_TAG = "My_TAG";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(My_TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int tm = intent.getIntExtra("time", 0);
        String label = intent.getStringExtra("label");
        Log.d(My_TAG, "onHandleIntent start " + label);
        try {
            TimeUnit.SECONDS.sleep(tm);
        } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        Log.d (My_TAG, "onHandleIntent end " + label);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(My_TAG, "onDestroy");
    }
}
