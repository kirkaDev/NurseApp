package com.desiredsoftware.nurseapp;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.WorkerThread;
import androidx.work.Worker;

public class Timer extends CountDownTimer {

    static final String LOG_TAG = "Timer: ";
    IPlay spPlayer;

    public Timer(long millisInFuture, long countDownInterval, IPlay spPlayer) {
        super(millisInFuture, countDownInterval);
        this.spPlayer = spPlayer;
    }

    @Override
    public void onTick(long l) {
    }

    @Override
    public void onFinish() {
        spPlayer.StopSound();
        spPlayer.PlaySound();
    }


}
