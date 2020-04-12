package com.desiredsoftware.nurseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Context;
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

    IPlay soundPoolPlayer;
    ISchedule scheduleExmpl;

    ArrayList<Time> scheduleArray;

    TextView textViewTimePoints;

    Button btn_wakeUpTime;
    Button btn_toSleepTime;
    EditText editText_notificationsAmount;
    EditText editText_soundRepeatAmount;

    final static int SOUND_REPEAT_AMOUNT = 2;

    final static  String LOG_TAG = "MainActivity class: ";
    final static String TAG_PICKER_WAKE_UP = "PickerWakeUp";
    final static String TAG_PICKER_TO_SLEEP = "PickerToSleep";
    final static String TIME_PICKER_WAKEUP = "WAKEUP";
    final static String TIME_PICKER_TO_SLEEP = "SLEEP";

    private String whatTimePickerWasPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Landcape layout
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        scheduleExmpl = new Schedule();
        scheduleArray =  scheduleExmpl.GetSchedule();

        btn_wakeUpTime = findViewById(R.id.btn_wakeUpTime);
        btn_wakeUpTime.setText(scheduleExmpl.GetWakeupTime().getTimeValue()[0] + ":" + scheduleExmpl.GetWakeupTime().getTimeValue()[1]);

        btn_toSleepTime = findViewById(R.id.btn_tpSleepTime);
        btn_toSleepTime.setText(scheduleExmpl.GetToSleepTime().getTimeValue()[0] + ":" + scheduleExmpl.GetToSleepTime().getTimeValue()[1]);

        editText_notificationsAmount = findViewById(R.id.editText_notificationsAmount);
        editText_soundRepeatAmount = findViewById(R.id.editText_soundRepeatAmount);
        textViewTimePoints = findViewById(R.id.textViewTimePoints);

        loadDefaultSettings();
        // loadSavedSettings();

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
                {
                    notificationsFromEditText = Integer.valueOf(editable.toString()).intValue();
                }

                if (notificationsFromEditText >1) {
                    scheduleExmpl.SetNotificationsPerDay(notificationsFromEditText);
                    refreshSchedule();
                    showSchedule();
                }
                else
                {
                    Log.d(LOG_TAG, "Пользователь ввел некорректное количество напоминаний: " + notificationsFromEditText);
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

        soundPoolPlayer = new SoundPoolPlayer(this, (AudioManager) getSystemService(Context.AUDIO_SERVICE));

        showSchedule();

    }

    public void onClickPlaySound(View v) {
        soundPoolPlayer.playSound(Integer.parseInt(editText_soundRepeatAmount.getText().toString()));
        //startService(new Intent(this, AwakeNurseService.class).putExtra("time", 2).putExtra("label","Call 1"));
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

    void loadDefaultSettings()
    {
        editText_notificationsAmount.setText(String.valueOf(scheduleExmpl.GetCurrentNotificationsPerDay()));
        editText_soundRepeatAmount.setText(String.valueOf(SOUND_REPEAT_AMOUNT));
    }

    void showSchedule()
    {
        int firstPointToShow = 0;
        boolean willShow = false;
        textViewTimePoints.setText("");

        ListIterator<Time> listIterator = scheduleArray.listIterator();

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
