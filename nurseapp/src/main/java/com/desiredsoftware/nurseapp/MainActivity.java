package com.desiredsoftware.nurseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

public class  MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    IPlay soundPoolPlayer;
    ISchedule scheduleExmpl;

    ArrayList<Time> currentSchedule;

    TextView textViewTimePoints;

    Button btn_wakeUpTime;
    Button btn_toSleepTime;
    EditText editText_notificationsAmount;
    EditText editText_soundRepeatAmount;

    final int SOUND_REPEAT_AMOUNT = 2;
    final static String TAG_PICKER_WAKE_UP = "PickerWakeUp";
    final static String TAG_PICKER_TO_SLEEP = "PickerToSleep";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_wakeUpTime = findViewById(R.id.btn_wakeUpTime);
        btn_toSleepTime = findViewById(R.id.btn_tpSleepTime);
        editText_notificationsAmount = findViewById(R.id.editText_notificationsAmount);
        editText_soundRepeatAmount = findViewById(R.id.editText_soundRepeatAmount);
        textViewTimePoints = findViewById(R.id.textViewTimePoints);

        scheduleExmpl = new Schedule();
        currentSchedule =  scheduleExmpl.GetSchedule();

        loadDefaultSettings();
        // loadSavedSettings();

        btn_wakeUpTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), TAG_PICKER_WAKE_UP);
            }
        });

        btn_toSleepTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
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
            String tag = timePicker.getTag().toString();
            btn_wakeUpTime.setText(hours + ":" + minutes);
            timePicker.getTag();
    }

    void loadDefaultSettings()
    {
        editText_notificationsAmount.setText(String.valueOf(scheduleExmpl.GetCurrentNotificationsPerDay()));
        editText_soundRepeatAmount.setText(String.valueOf(SOUND_REPEAT_AMOUNT));
    }

    void showSchedule()
    {
        for (Time tPoint: currentSchedule)
        {
            int[] tPoints = tPoint.getTimeValue();
            textViewTimePoints.append(tPoints[0] + ":" + tPoints[1] + ":" + tPoints[2] + "\n\n");
        }
    }
}
