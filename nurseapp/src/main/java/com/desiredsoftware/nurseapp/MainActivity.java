package com.desiredsoftware.nurseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class  MainActivity extends AppCompatActivity {

    IPlay mSoundPoolPlayer;
    ISchedule mScheduleExmpl;

    ArrayList<Time> mCurrentSchedule;

    TextView textViewTimePoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimePoints = findViewById(R.id.textViewTimePoints);

        mSoundPoolPlayer = new SoundPoolPlayer(this, (AudioManager) getSystemService(Context.AUDIO_SERVICE));

        mScheduleExmpl = new Schedule();
        mCurrentSchedule =  mScheduleExmpl.GetSchedule();

        for (Time tPoint: mCurrentSchedule)
        {
            int[] tPoints = tPoint.getTimeValue();
            textViewTimePoints.append(tPoints[0] + ":" + tPoints[1] + ":" + tPoints[2] + "\n\n");
        }
    }

    public void onClickPlaySound(View v) {
        mSoundPoolPlayer.playSound(1);
        //startService(new Intent(this, AwakeNurseService.class).putExtra("time", 2).putExtra("label","Call 1"));
    }

}
