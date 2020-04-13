package com.desiredsoftware.nurseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Context;
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

    private int soundRepeatAmount = 0;
    private Time wakeUpTime;
    private Time toSleepTime;

    private String whatTimePickerWasPressed;
    private boolean isPlaying = false;

    IPlay soundPoolPlayer;
    ISchedule scheduleExmpl;

    ArrayList<Time> scheduleArray;
    ArrayList<Timer> timerArray;

    SharedPreferences savedSettings;

    final static String APP_PREFERENCES = "mysettings";

    final static String PREFERENCES_WAKEUP_TIME = "wakeup";
    final static String PREFERENCES_TO_SLEEP_TIME = "sleep";
    final static String PREFERENCES_SOUND_REPEAT_AMOUNT = "sound_repeat";
    final static String PREFERENCES_NOTIFICATIONS_AMOUNT = "notifications_amount";


    TextView textViewTimePoints;

    Button btn_wakeUpTime;
    Button btn_toSleepTime;

    EditText editText_notificationsAmount;
    EditText editText_soundRepeatAmount;
    EditText editText_timerPeriod;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // TODO: Landcape layout
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        savedSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        scheduleExmpl = new Schedule();
        scheduleArray =  scheduleExmpl.GetSchedule();

        timerArray = new ArrayList<Timer>();



        btn_wakeUpTime = findViewById(R.id.btn_wakeUpTime);
        btn_wakeUpTime.setText(scheduleExmpl.GetWakeupTime().getTimeValue()[0] + ":" + scheduleExmpl.GetWakeupTime().getTimeValue()[1]);

        btn_toSleepTime = findViewById(R.id.btn_tpSleepTime);
        btn_toSleepTime.setText(scheduleExmpl.GetToSleepTime().getTimeValue()[0] + ":" + scheduleExmpl.GetToSleepTime().getTimeValue()[1]);

        editText_notificationsAmount = findViewById(R.id.editText_notificationsAmount);
        editText_soundRepeatAmount = findViewById(R.id.editText_soundRepeatAmount);
        editText_timerPeriod = findViewById(R.id.editText_timerPeriod);

        textViewTimePoints = findViewById(R.id.textViewTimePoints);

        loadSavedSettings();

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
                { notificationsFromEditText = Integer.valueOf(editable.toString()).intValue(); }

                if (notificationsFromEditText >1) {
                    scheduleExmpl.SetNotificationsPerDay(notificationsFromEditText);
                    refreshSchedule();
                    showSchedule();
                }
                else
                { Log.d(LOG_TAG, "Пользователь ввел некорректное количество напоминаний: " + notificationsFromEditText); }
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

        soundPoolPlayer = new SoundPoolPlayer(this, (AudioManager) getSystemService(Context.AUDIO_SERVICE));

        showSchedule();

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

    public void onClickPlaySound(View v) {

        int timePeriod = Integer.parseInt(editText_timerPeriod.getText().toString())*1000;
        int soundRepeatAmount = Integer.parseInt(editText_soundRepeatAmount.getText().toString());

        Timer timer = new Timer(timePeriod, timePeriod / 1000, soundPoolPlayer, soundRepeatAmount);

        if (!isPlaying) {
            isPlaying = true;
            timer.start();
        }
        else
        {
            isPlaying = true;
            soundPoolPlayer.stopSound();
            timer.cancel();
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {

        String strToSet = hours + ":" + minutes;

            switch (whatTimePickerWasPressed)
            {

                case TIME_PICKER_WAKEUP:
                {
                    String txtFromBtnSleepTime = btn_toSleepTime.getText().toString();

                    if (btn_toSleepTime.getText().toString().equals(strToSet))
                    {
                        Toast.makeText(this,"Время подъема и отбоя не должно быть одинаковым, " +
                                "введите, пожалуйста, выберете другок время", Toast.LENGTH_LONG).show();
                        return;
                    }
                    btn_wakeUpTime.setText(strToSet);
                    scheduleExmpl.SetWakeupTime(new Time(hours, minutes, 0));
                    break;
                }

                case TIME_PICKER_TO_SLEEP:
                {
                    if (btn_wakeUpTime.getText().toString().equals(strToSet))
                    {
                        Toast.makeText(this,"Время подъема и отбоя не должно быть одинаковым, " +
                                "введите, пожалуйста, выберете другок время", Toast.LENGTH_LONG).show();
                        return;
                    }

                    btn_toSleepTime.setText(strToSet);
                    scheduleExmpl.SetToSleepTime(new Time(hours, minutes, 0));
                    break;
                }
            }
            refreshSchedule();
            showSchedule();
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

        editText_soundRepeatAmount.setText(String.valueOf(savedSettings.getInt(PREFERENCES_SOUND_REPEAT_AMOUNT,1)));
        editText_notificationsAmount.setText(String.valueOf(savedSettings.getInt(PREFERENCES_NOTIFICATIONS_AMOUNT,3)));
    }

    void showSchedule()
    {
        int firstPointToShow = 0;
        boolean willShow = false;
        textViewTimePoints.setText("");

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

                String str = editText_soundRepeatAmount.getText().toString();
                int soundRepeatAmount = Integer.parseInt(editText_soundRepeatAmount.getText().toString());

                Timer timer = new Timer(timePeriod, timePeriod / 1000, soundPoolPlayer, soundRepeatAmount);
                timerArray.add(timer);
                timer.start();

                textViewTimePoints.append(tPoints[0] + ":" + tPoints[1] + ":" + tPoints[2] + "\n\n");
                willShow = true;
            }
        }

        if (!willShow)
        {
            textViewTimePoints.append("Почему вы не спите?");
        }

    }

    void showScheduleInFull()
    {
        for (Time tPoint: scheduleArray)
        {
            int[] tPoints = tPoint.getTimeValue();

            if (Time.getTimeValueOnSeconds(Time.getCurrentTime())< Time.getTimeValueOnSeconds(tPoint)) {
                textViewTimePoints.append(tPoints[0] + ":" + tPoints[1] + ":" + tPoints[2] + "\n\n");
            }
        }
    }

    void refreshSchedule()
    {
        scheduleArray = scheduleExmpl.GetSchedule();
    }
}
