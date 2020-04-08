package com.desiredsoftware.nurseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickStart(View v)
    {
        startService(new Intent(this, AwakeNurseService.class).putExtra("time", 2).putExtra("label","Call 1"));
    }


}
