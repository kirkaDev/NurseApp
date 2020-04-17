package com.desiredsoftware.nurseapp;

import android.os.CountDownTimer;

public class Timer extends CountDownTimer {

    static final String LOG_TAG = "Timer: ";
    IPlay spPlayer;

    interface CountDownIsOver {
        void TimerCallback();
    }

    CountDownIsOver countDownIsOver;

    void registerCallBack(CountDownIsOver callback){
        this.countDownIsOver = callback;
    }

    void doSomething(){
//  countDownIsOver.TimerСallback();
    }


    public Timer(long millisInFuture, long countDownInterval, IPlay spPlayer) {
        super(millisInFuture, countDownInterval);
        this.spPlayer = spPlayer;
    }

    @Override
    public void onTick(long l) {
    }

    @Override
    public void onFinish() {
        countDownIsOver.TimerCallback();
        spPlayer.StopSound();
        spPlayer.PlaySound();

    }


}
