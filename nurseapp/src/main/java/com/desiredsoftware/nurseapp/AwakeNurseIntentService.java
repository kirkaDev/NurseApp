package com.desiredsoftware.nurseapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AwakeNurseIntentService extends IntentService implements Timer.CountDownIsOver{
    public AwakeNurseIntentService() {
        super("AwakeNurseIntentService");
    }

    String My_TAG = "AwakeNurseIntentService ";

    IPlay soundPoolPlayer;
    ISchedule schedule;

    ArrayList<Time> scheduleArray;

    Timer currentTimer;

    SharedPreferences savedSettings;

    final static String APP_PREFERENCES = "mysettings";

    final static String PREFERENCES_WAKEUP_TIME = "wakeup";
    final static String PREFERENCES_TO_SLEEP_TIME = "sleep";
    final static String PREFERENCES_SOUND_REPEAT_AMOUNT = "sound_repeat";
    final static String PREFERENCES_NOTIFICATIONS_AMOUNT = "notifications_amount";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(My_TAG, "onCreate");

        savedSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        loadSavedSettings();

        scheduleArray =  schedule.GetScheduleList();
        runSchedule();
    }

    // Сервис не остановится самовольно
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);

       // runSchedule();

        int[] newWakeupTime = intent.getIntArrayExtra("wakeupTime");
        int[] newToSleepTime = intent.getIntArrayExtra("toSleepTime");
        int notificationsAmount = intent.getIntExtra("notificationsAmount", 8);
        int soundRepeatAmount = intent.getIntExtra("soundRepeatAmount", 1);

        if (startId!=1)
        {
            schedule.SetWakeupTime(new Time(newWakeupTime));
            schedule.SetToSleepTime(new Time(newToSleepTime));
            schedule.SetNotificationsPerDay(notificationsAmount);
            soundPoolPlayer = new SoundPoolPlayer(this, (AudioManager) getSystemService(Context.AUDIO_SERVICE), soundRepeatAmount);
            this.scheduleArray = schedule.GetScheduleList();
            runSchedule();
        }

        return START_STICKY;
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

        runSchedule();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(My_TAG, "onDestroy");
    }

    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    void loadSavedSettings()
    {
        int notifications = savedSettings.getInt(PREFERENCES_NOTIFICATIONS_AMOUNT,7);
        int soundRepeatAmount = savedSettings.getInt(PREFERENCES_SOUND_REPEAT_AMOUNT,1);
        String toLoadWakeupTime = savedSettings.getString(PREFERENCES_WAKEUP_TIME, "9:00");
        String toLoadSleepTime = savedSettings.getString(PREFERENCES_TO_SLEEP_TIME, "21:00");

        soundPoolPlayer = new SoundPoolPlayer(this, (AudioManager) getSystemService(Context.AUDIO_SERVICE), soundRepeatAmount);
        schedule = new Schedule(notifications, Time.fromStringToTime(toLoadWakeupTime), Time.fromStringToTime(toLoadSleepTime));
    }


    void runSchedule()
    {
        if (currentTimer!=null)
            currentTimer.cancel();

        int currentPosition= schedule.GetFirstNotificationIndex(Time.getCurrentTime());

        Time firstNotification = scheduleArray.get(currentPosition);
        int timePeriod = Time.getTimeValueOnSeconds(Time.GetTimeInterval(Time.getCurrentTime(), firstNotification))*1000;

        this.currentTimer = new Timer(timePeriod, timePeriod / 1000, this.soundPoolPlayer);
        currentTimer.registerCallBack(this);
        currentTimer.start();
    }

    void setNextNotification()
    {
        Time nextNotification = schedule.GetNextNotifaction();
        int timePeriod = Time.getTimeValueOnSeconds(Time.GetTimeInterval(Time.getCurrentTime(), nextNotification))*1000;

        this.currentTimer = new Timer(timePeriod, timePeriod / 1000, this.soundPoolPlayer);
        this.currentTimer.registerCallBack(this);
        this.currentTimer.start();
    }

    @Override
    public void TimerCallback() {
        setNextNotification();
    }

}
