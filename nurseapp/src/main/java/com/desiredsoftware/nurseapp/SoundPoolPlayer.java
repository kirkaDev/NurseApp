package com.desiredsoftware.nurseapp;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;

// This class implements playing audio using sound pool class.
// Files for playing will be take from assets folder.
public class SoundPoolPlayer implements IPlay {

    private Object currentSound;

    private static String pathFolderForSounds = "audio";


    int repetitionsAmount;

    private SoundPool sPool;

    private int maxStreams = 10;
    private int streamType = AudioManager.STREAM_ALARM;
    private int srcQuality = 0;

    AssetManager assetManager;

    SoundPoolPlayer()
    {
        sPool = new SoundPool(maxStreams, streamType, srcQuality);
    }




    @Override
    public void playSound() {

    }

    @Override
    public void stopSound() {

    }


    @Override
    public void repeatSound(int repetitionsAmount) {
        for (int i=0; i<repetitionsAmount; i++)
        {
            playSound();
        }
    }

    public Object getCurrentSound() {
        return currentSound;
    }

    public void setCurrentSound(Object currentSound) {
        this.currentSound = currentSound;
    }

    }
