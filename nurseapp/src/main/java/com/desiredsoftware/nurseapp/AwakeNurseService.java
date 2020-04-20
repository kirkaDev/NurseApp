package com.desiredsoftware.nurseapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class AwakeNurseService extends Service implements Timer.CountDownIsOver {


    static final String NOTIFICATION_TEXT = "Пора принять лекарство ";
    static final String NEXT_NOTIFICATION_TEXT = "Следующее напоминание в ";

    String My_TAG = "AwakeNurseService ";

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

    Notification currentNotification;
    PendingIntent pendingIntentForStartActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(My_TAG, "onCreate");
        Toast.makeText(this, "Работа в фоновом режиме запущена", Toast.LENGTH_LONG).show();

        savedSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        loadSavedSettings();
        scheduleArray =  schedule.GetScheduleList();

        runSchedule();

        Intent intentActivity = new Intent(this, MainActivity.class);

        pendingIntentForStartActivity = PendingIntent.getActivity(this, 0 ,
                intentActivity, PendingIntent.FLAG_CANCEL_CURRENT);


        currentNotification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("NurseApp")
                .setContentText(NEXT_NOTIFICATION_TEXT + scheduleArray.get(schedule.GetFirstNotificationIndex(Time.getCurrentTime())).getTimeValue()[0]+ ":"
                        + scheduleArray.get(schedule.GetFirstNotificationIndex(Time.getCurrentTime())).getTimeValue()[1] + ":" +
                        scheduleArray.get(schedule.GetFirstNotificationIndex(Time.getCurrentTime())).getTimeValue()[2])
                .setContentIntent(pendingIntentForStartActivity)
                .build();

        startForeground(1, currentNotification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int[] newWakeupTime = intent.getIntArrayExtra("wakeupTime");
        int[] newToSleepTime = intent.getIntArrayExtra("toSleepTime");
        int notificationsAmount = intent.getIntExtra("notificationsAmount", 8);
        int soundRepeatAmount = intent.getIntExtra("soundRepeatAmount", 1);

        if (newWakeupTime!=null && newToSleepTime!=null)
        {
            schedule.SetWakeupTime(new Time(newWakeupTime));
            schedule.SetToSleepTime(new Time(newToSleepTime));
            schedule.SetNotificationsPerDay(notificationsAmount);
            soundPoolPlayer = new SoundPoolPlayer(this, (AudioManager) getSystemService(Context.AUDIO_SERVICE), soundRepeatAmount);
            this.scheduleArray = schedule.GetScheduleList();

            runSchedule();

            currentNotification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("NurseApp")
                    .setContentText(NEXT_NOTIFICATION_TEXT + scheduleArray.get(schedule.GetFirstNotificationIndex(Time.getCurrentTime())).getTimeValue()[0]+ ":"
                            + scheduleArray.get(schedule.GetFirstNotificationIndex(Time.getCurrentTime())).getTimeValue()[1] + ":" +
                            scheduleArray.get(schedule.GetFirstNotificationIndex(Time.getCurrentTime())).getTimeValue()[2])
                    .setContentIntent(pendingIntentForStartActivity)
                    .build();

            startForeground(1, currentNotification);
        }

        if (intent.getBooleanExtra("isToMute", false))
            currentTimer.cancel();
        else
            runSchedule();



        return START_STICKY;
    }

    void muteSchedule(boolean isToMute)
    {
        if (isToMute)
        {
            currentTimer.cancel();
        }
        else
        {
            runSchedule();
        }
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



        Toast.makeText(this, NEXT_NOTIFICATION_TEXT + firstNotification.getTimeValue()[0]+ ":"
                + firstNotification.getTimeValue()[1] + ":" +
                firstNotification.getTimeValue()[2], Toast.LENGTH_LONG).show();

        int timePeriod = Time.getTimeValueOnSeconds(Time.GetTimeInterval(Time.getCurrentTime(), firstNotification))*1000;

        this.currentTimer = new Timer(timePeriod, timePeriod / 1000, this.soundPoolPlayer);
        currentTimer.registerCallBack(this);
        currentTimer.start();
    }

    void setNextNotification()
    {
        Time nextNotification = schedule.GetNextNotifaction();
        int timePeriod = Time.getTimeValueOnSeconds(Time.GetTimeInterval(Time.getCurrentTime(), nextNotification))*1000;

        Toast.makeText(this, NEXT_NOTIFICATION_TEXT + nextNotification.getTimeValue()[0]+ ":"
                        + nextNotification.getTimeValue()[1] + ":" +
                            nextNotification.getTimeValue()[2], Toast.LENGTH_LONG).show();

        String stringForPrint = NEXT_NOTIFICATION_TEXT + nextNotification.getTimeValue()[0]+ ":"
                + nextNotification.getTimeValue()[1] + ":" +
                nextNotification.getTimeValue()[2];

        currentNotification = new NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("NurseApp")
            .setContentText(stringForPrint)
            .setContentIntent(pendingIntentForStartActivity)
            .build();

        startForeground(1, currentNotification);

        this.currentTimer = new Timer(timePeriod, timePeriod / 1000, this.soundPoolPlayer);
        this.currentTimer.registerCallBack(this);
        this.currentTimer.start();
    }

    @Override
    public void TimerCallback() {
        Toast.makeText(this, NOTIFICATION_TEXT, Toast.LENGTH_LONG).show();
        setNextNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(My_TAG, "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

