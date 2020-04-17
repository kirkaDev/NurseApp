package com.desiredsoftware.nurseapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

public class AwakeNurseService extends IntentService {
    public AwakeNurseService() {
        super("My awake nurse service");
    }

    String My_TAG = "AwakeNurseService ";

    IPlay soundPoolPlayer;
    ISchedule schedule;

    ArrayList<Time> scheduleArray;
    ArrayList<Timer> timerArray;

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

        timerArray = new ArrayList<Timer>();

        runSchedule();
        showSchedule();
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

    public void PlaySound() {

        int timePeriod = 10000;
        int soundRepeatAmount = 2;

        Timer timer = new Timer(timePeriod, timePeriod / 1000, soundPoolPlayer);
        timer.start();
        }

    void loadSavedSettings()
    {
        int notifications = savedSettings.getInt(PREFERENCES_NOTIFICATIONS_AMOUNT,8);
        int soundRepeatAmount = savedSettings.getInt(PREFERENCES_SOUND_REPEAT_AMOUNT,1);
        String toLoadWakeupTime = savedSettings.getString(PREFERENCES_WAKEUP_TIME, "9:00");
        String toLoadSleepTime = savedSettings.getString(PREFERENCES_TO_SLEEP_TIME, "22:00");

        soundPoolPlayer = new SoundPoolPlayer(this, (AudioManager) getSystemService(Context.AUDIO_SERVICE), soundRepeatAmount);
        schedule = new Schedule(notifications, Time.fromStringToTime(toLoadWakeupTime), Time.fromStringToTime(toLoadSleepTime));
    }

    void showSchedule()
    {
        int firstPointToShow = 0;
        boolean willShow = false;

        ListIterator<Time> listIterator = scheduleArray.listIterator();

        for (Timer timer: timerArray)
        {
            timer.cancel();
            timer = null;
        }

        timerArray = null;

        timerArray = new ArrayList<Timer>();

        while (listIterator.hasNext())
        {

            if (Time.getTimeValueOnSeconds(Time.getCurrentTime()) > Time.getTimeValueOnSeconds(listIterator.next()))
            {
                firstPointToShow++;
                continue;
            }
            else
            {
                break;
            }
        }

        listIterator = null;
        listIterator = scheduleArray.listIterator();

        while (listIterator.hasNext())
        {
            Time tPoint = listIterator.next();
            int[] tPoints = tPoint.getTimeValue();
            if ((listIterator.nextIndex()-1)>= firstPointToShow)
            {
                int timePeriod = Time.getTimeValueOnSeconds(Time.GetTimeInterval(Time.getCurrentTime(), tPoint))*1000;

                Timer timer = new Timer(timePeriod, timePeriod / 1000, soundPoolPlayer);
                timerArray.add(timer);
                timer.start();

//                textViewTimePoints.append(tPoints[0] + ":" + tPoints[1] + ":" + tPoints[2] + "\n\n");
                willShow = true;
            }
        }

        if (!willShow)
        {
//            textViewTimePoints.append("Почему вы не спите?");
        }

    }

    void showScheduleInFull()
    {
        for (Time tPoint: scheduleArray)
        {
            int[] tPoints = tPoint.getTimeValue();

            if (Time.getTimeValueOnSeconds(Time.getCurrentTime())< Time.getTimeValueOnSeconds(tPoint)) {
//                textViewTimePoints.append(tPoints[0] + ":" + tPoints[1] + ":" + tPoints[2] + "\n\n");
            }
        }
    }

    void refreshSchedule()
    {
        scheduleArray = schedule.GetScheduleList();
    }

    void runSchedule()
    {
        int index = schedule.GetFirstNotificationIndex(Time.getCurrentTime());

        int timePeriod = Time.getTimeValueOnSeconds(Time.GetTimeInterval(Time.getCurrentTime(), scheduleArray.get(index)))*1000;

        Timer timer = new Timer(timePeriod, 1000, soundPoolPlayer);
        timerArray.add(timer);
        timer.start();
    }
}
