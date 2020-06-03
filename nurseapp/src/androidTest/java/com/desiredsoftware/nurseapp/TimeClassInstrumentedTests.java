package com.desiredsoftware.nurseapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.testing.TestWorkerBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TimeClassInstrumentedTests {

    private Context mContext;
    private Executor mExecutor;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.desiredsoftware.nurseapp", appContext.getPackageName());
    }

    @Test
    public void getCurrentTimeIntArr() {
        Time myDeviceTime = new Time();

        Time currentTime = myDeviceTime.getCurrentTime();
        assertNotEquals(new int[]{-1,-1,-1,}, currentTime);
    }

    @Test
    public void playSound() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        int repeatAmount = 1;

        IPlay spPlayer = new SoundPoolPlayer(appContext, (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE), repeatAmount);

        Thread.sleep(2000);

        spPlayer.PlaySound();

        Thread.sleep(2000);

        assertTrue(false);
    }

    @Test
    public void timerVerify() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        int repeatNumber = 1;
        IPlay spPlayer = new SoundPoolPlayer(appContext, (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE), repeatNumber);

        Timer timer = new Timer(2000, 1000, spPlayer);
        timer.start();

        Thread.sleep(8000);

        assertTrue(false);
    }

    @Before
    public void setUp()
    {
        mContext = ApplicationProvider.getApplicationContext();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Test
    public void OneTimeWorkNotifyTest()
    {
        //Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Data inputData = new Data.Builder().putLong("SLEEP_DURATION", 10_000L).build();

        WorkerNurse worker = (WorkerNurse) TestWorkerBuilder.from(mContext, WorkerNurse.class,
                                                                    mExecutor).setInputData(inputData).build();

        OneTimeWorkRequest testWorkRequest = new OneTimeWorkRequest.Builder(WorkerNurse.class)
                .setInitialDelay(5, TimeUnit.SECONDS)
                .build();



        WorkManager.getInstance(mContext).enqueue(testWorkRequest);

        //ListenableWorker.Result result = worker.doWork();

        //assertThat(result, is(ListenableWorker.Result.success()));
    }

}
