package com.desiredsoftware.nurseapp;

import android.content.Context;
import android.media.AudioManager;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TimeClassInstrumentedTests {
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

        IPlay spPlayer = new SoundPoolPlayer(appContext, (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE));

        Thread.sleep(2000);

        spPlayer.playSound(repeatAmount);

        Thread.sleep(2000);

        assertTrue(false);
    }

}
