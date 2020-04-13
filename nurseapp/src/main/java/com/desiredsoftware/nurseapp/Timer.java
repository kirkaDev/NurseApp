package com.desiredsoftware.nurseapp;

import android.os.CountDownTimer;
import android.util.Log;

public class Timer extends CountDownTimer {

    IPlay spPlayer;
    int repeatNumber;

    public Timer(long millisInFuture, long countDownInterval, IPlay spPlayer, int repeatNumber) {
        super(millisInFuture, countDownInterval);
        this.spPlayer = spPlayer;
        this.repeatNumber = repeatNumber;
    }

    @Override
    public void onTick(long l) {
    }

    @Override
    public void onFinish() {

        spPlayer.stopSound();
        spPlayer.playSound(repeatNumber);

    }
}
