package com.desiredsoftware.nurseapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkerNurse extends Worker {
    public WorkerNurse(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        //spPlayer = new SoundPoolPlayer();
    }


    static final String LOG_TAG = "WorkManager: ";
    IPlay spPlayer;

    @NonNull
    @Override
    public Result doWork() {
        Log.d(LOG_TAG, "Выполняю метод doWork()");
        spPlayer.StopSound();
        spPlayer.PlaySound();
        return Result.success();
    }
}
