package com.desiredsoftware.nurseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.ListIterator;

public class  MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    final static String LOG_TAG = "MainActivity class: ";
    final static String TAG_PICKER_WAKE_UP = "PickerWakeUp";
    final static String TAG_PICKER_TO_SLEEP = "PickerToSleep";
    final static String TIME_PICKER_WAKEUP = "WAKEUP";
    final static String TIME_PICKER_TO_SLEEP = "SLEEP";


    private String whatTimePickerWasPressed;

    private boolean isToMute = false;

    IPlay soundPoolPlayer;
    ISchedule schedule;

    ArrayList<Time> scheduleArray;

    SharedPreferences savedSettings;

    final static String APP_PREFERENCES = "mysettings";
    final static String PREFERENCES_WAKEUP_TIME = "wakeup";
    final static String PREFERENCES_TO_SLEEP_TIME = "sleep";
    final static String PREFERENCES_SOUND_REPEAT_AMOUNT = "sound_repeat";
    final static String PREFERENCES_NOTIFICATIONS_AMOUNT = "notifications_amount";

    TextView textViewTimePoints;
    TextView textViewNextNotificationsNPoints;
    TextView txtView_nextNotificationNumber;

    Button btn_wakeUpTime;
    Button btn_toSleepTime;
    Button btn_stopSchedule;


    EditText editText_notificationsAmount;
    EditText editText_soundRepeatAmount;
    EditText editText_showNextNotificationsNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Landcape layout
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btn_stopSchedule = findViewById(R.id.btn_stopSchedule);
        btn_wakeUpTime = findViewById(R.id.btn_wakeUpTime);
        btn_toSleepTime = findViewById(R.id.btn_tpSleepTime);
        editText_notificationsAmount = findViewById(R.id.editText_notificationsAmount);
        editText_soundRepeatAmount = findViewById(R.id.editText_soundRepeatAmount);
        editText_showNextNotificationsNumber = findViewById(R.id.editText_showNextNotificationsNumber);
        textViewTimePoints = findViewById(R.id.textViewTimePoints);
        textViewNextNotificationsNPoints = findViewById(R.id.textViewNextNotificationsNPoints);
        txtView_nextNotificationNumber = findViewById(R.id.txtView_nextNotificationNumber);

        savedSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        loadSavedSettings();

        scheduleArray =  schedule.GetScheduleList();

        editText_notificationsAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                int notificationsFromEditText = 0;

                if (!editable.toString().equals(""))
                { notificationsFromEditText = Integer.valueOf(editable.toString()); }

                if (notificationsFromEditText >1) {
                    schedule.SetNotificationsPerDay(notificationsFromEditText);
                    scheduleArray = schedule.GetScheduleList();
                    refreshNotificationsNumber(notificationsFromEditText);
                    showSchedule();
                    showNextNumberNotifications();
                }
                else
                { Log.d(LOG_TAG, "Вы ввели некорректное количество напоминаний: " + notificationsFromEditText +
                "количество напоминаний должно быть 2 или более");
                }
            }
        });

        editText_soundRepeatAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                int soundRepeatFromEditText;

                if (!editable.toString().equals(""))
                { soundRepeatFromEditText = Integer.valueOf(editable.toString());
                    soundPoolPlayer.SetRepeatAmount(soundRepeatFromEditText);
                    refreshRepeatNumber(soundRepeatFromEditText);
                }
            }
        });

        editText_showNextNotificationsNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                int notificationsNumberToShow = 0;

                if (!editable.toString().equals(""))
                { notificationsNumberToShow = Integer.valueOf(editable.toString()); }

                if (notificationsNumberToShow >1) {
                    showNextNumberNotifications();
                }
            }
        });


        btn_wakeUpTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                whatTimePickerWasPressed = TIME_PICKER_WAKEUP;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), TAG_PICKER_WAKE_UP);
            }
        });

        btn_toSleepTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                whatTimePickerWasPressed = TIME_PICKER_TO_SLEEP;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), TAG_PICKER_TO_SLEEP);
            }
        });

        btn_stopSchedule.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (isToMute)
               {
                   btn_stopSchedule.setText("Выключить");
                   isToMute = false;
               }
               else
               {
                   isToMute = true;
                   btn_stopSchedule.setText("Включить");
               }
               muteSchedule(isToMute);
           }
       });

        showSchedule();
        showNextNumberNotifications();

        startService(new Intent(this, AwakeNurseService.class));

    }

    void muteSchedule(boolean isToMute)
    {
        startService(new Intent(this, AwakeNurseService.class).putExtra("isToMute", isToMute));
    }

    void refreshRepeatNumber(int soundRepeat)
    {
        startService(new Intent(this, AwakeNurseService.class).
                putExtra("wakeupTime", schedule.GetWakeupTime().getTimeValue()).
                putExtra("toSleepTime", schedule.GetToSleepTime().getTimeValue()).
                putExtra("notificationsAmount", schedule.GetNotificationsPerDay()).
                putExtra("soundRepeatAmount", soundRepeat));
    }

    void refreshNotificationsNumber(int notificationsPerDay)
    {
        startService(new Intent(this, AwakeNurseService.class).
                putExtra("wakeupTime", schedule.GetWakeupTime().getTimeValue()).
                putExtra("toSleepTime", schedule.GetToSleepTime().getTimeValue()).
                putExtra("notificationsAmount", notificationsPerDay).
                putExtra("soundRepeatAmount", Integer.parseInt(editText_soundRepeatAmount.getText().toString())));
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPreferences();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSharedPreferences();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {

        String strToSet = hours + ":" + minutes;

            switch (whatTimePickerWasPressed)
            {
                case TIME_PICKER_WAKEUP:
                {
                    if (btn_toSleepTime.getText().toString().equals(strToSet))
                    {
                        Toast.makeText(this,"Время подъема и отбоя не должно быть одинаковым, " +
                                "введите, пожалуйста, выберете другок время", Toast.LENGTH_LONG).show();
                        return;
                    }
                    btn_wakeUpTime.setText(strToSet);
                    schedule.SetWakeupTime(new Time(hours, minutes, 0));

                    startService(new Intent(this, AwakeNurseService.class).
                            putExtra("wakeupTime", schedule.GetWakeupTime().getTimeValue()).
                            putExtra("toSleepTime", schedule.GetToSleepTime().getTimeValue()).
                            putExtra("notificationsAmount", schedule.GetNotificationsPerDay()).
                            putExtra("soundRepeatAmount", Integer.parseInt(editText_soundRepeatAmount.getText().toString())));

                    break;
                }

                case TIME_PICKER_TO_SLEEP:
                {
                    if (btn_wakeUpTime.getText().toString().equals(strToSet))
                    {
                        Toast.makeText(this,"Время подъема и отбоя должно быть разным, " +
                                "выберете, пожалуйста, другое время", Toast.LENGTH_LONG).show();
                        return;
                    }
                    btn_toSleepTime.setText(strToSet);
                    schedule.SetToSleepTime(new Time(hours, minutes, 0));

                    startService(new Intent(this, AwakeNurseService.class).
                            putExtra("wakeupTime", schedule.GetWakeupTime().getTimeValue()).
                            putExtra("toSleepTime", schedule.GetToSleepTime().getTimeValue()).
                            putExtra("notificationsAmount", schedule.GetNotificationsPerDay()).
                            putExtra("soundRepeatAmount", Integer.parseInt(editText_soundRepeatAmount.getText().toString())));
                    break;
                }
            }
            scheduleArray = schedule.GetScheduleList();
            showSchedule();
            showNextNumberNotifications();
    }

    void saveSharedPreferences()
    {
        int soundRepeatAmount = Integer.parseInt(editText_soundRepeatAmount.getText().toString());
        int notificationsAmount = Integer.parseInt(editText_notificationsAmount.getText().toString());
        String wakeupTime = btn_wakeUpTime.getText().toString();
        String toSleepTime = btn_toSleepTime.getText().toString();

        SharedPreferences.Editor editor = savedSettings.edit();
        editor.putInt(PREFERENCES_NOTIFICATIONS_AMOUNT, notificationsAmount);
        editor.putInt(PREFERENCES_SOUND_REPEAT_AMOUNT, soundRepeatAmount);
        editor.putString(PREFERENCES_WAKEUP_TIME, wakeupTime);
        editor.putString(PREFERENCES_TO_SLEEP_TIME, toSleepTime);
        editor.apply();
    }

    void loadSavedSettings()
    {
        int notifications = savedSettings.getInt(PREFERENCES_NOTIFICATIONS_AMOUNT,7);
        int soundRepeatAmount = savedSettings.getInt(PREFERENCES_SOUND_REPEAT_AMOUNT,1);
        String toLoadWakeupTime = savedSettings.getString(PREFERENCES_WAKEUP_TIME, "9:00");
        String toLoadSleepTime = savedSettings.getString(PREFERENCES_TO_SLEEP_TIME, "21:00");

        soundPoolPlayer = new SoundPoolPlayer(this, (AudioManager) getSystemService(Context.AUDIO_SERVICE), soundRepeatAmount);
        schedule = new Schedule(notifications, Time.fromStringToTime(toLoadWakeupTime), Time.fromStringToTime(toLoadSleepTime));

        editText_notificationsAmount.setText(String.valueOf(notifications));
        editText_soundRepeatAmount.setText(String.valueOf(soundPoolPlayer.GetRepeatAmount()));
        btn_wakeUpTime.setText(toLoadWakeupTime);
        btn_toSleepTime.setText(toLoadSleepTime);
    }

    void showSchedule()
    {
        textViewTimePoints.setText("");

        ListIterator<Time> listIterator = scheduleArray.listIterator();

        listIterator = scheduleArray.listIterator();
        int firstPointToShow = schedule.GetFirstNotificationIndex(Time.getCurrentTime());


        while (listIterator.hasNext())
        {
            Time tPoint = listIterator.next();
            int[] tPoints = tPoint.getTimeValue();
            if ((listIterator.nextIndex()-1)>= firstPointToShow)
            {
                textViewTimePoints.append(tPoints[0] + ":" + tPoints[1] + ":" + tPoints[2] + "; ");
            }
        }
    }

    void showNextNumberNotifications()
    {
        int numberToShow = Integer.parseInt(editText_showNextNotificationsNumber.getText().toString());

        txtView_nextNotificationNumber.setText("Следующие " + numberToShow + " напоминаний(-я):");

        ArrayList<Time> arrToShow = schedule.GetNextNotifactions(numberToShow);
        ListIterator<Time> listIterator = arrToShow.listIterator();

        textViewNextNotificationsNPoints.setText("");

        while (listIterator.hasNext())
        {
            Time tPoint = listIterator.next();
            int[] tPoints = tPoint.getTimeValue();
            textViewNextNotificationsNPoints.append(tPoints[0] + ":" + tPoints[1] + ":" + tPoints[2] + "; ");
            }
    }
}
