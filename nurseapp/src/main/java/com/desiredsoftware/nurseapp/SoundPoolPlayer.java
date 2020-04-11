package com.desiredsoftware.nurseapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

// This class implements playing audio using sound pool class.
// Files for playing will be take from assets folder.
public class SoundPoolPlayer implements IPlay {

    int repetitionsAmount;

    private SoundPool soundPool;

    private int maxStreams = 2;
    private int streamType = AudioManager.STREAM_MUSIC;
    private int srcQuality = 0;

    float curVolume;
    float maxVolume;
    float leftVolume;
    float rightVolume;
    float playbackRate = 1f;

    int priority = 1;



    int repeatAmount = 0;
    int soundId = 1;
    int streamId;

    AssetManager assetManager;

    SoundPoolPlayer(Context content, AudioManager audioManager) {
        soundPool = new SoundPool(maxStreams, streamType, srcQuality);

        soundId = soundPool.load(content, R.raw.notify_sound, 1);

        curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        leftVolume = curVolume / maxVolume;
        rightVolume = curVolume / maxVolume;
    }

    @Override
    public void playSound(int repeatAmount)
    {
        setRepeatAmount(repeatAmount);
        streamId = soundPool.play(soundId, leftVolume, rightVolume, priority, this.repeatAmount, playbackRate);
    }

    @Override
    public void stopSound() {
        soundPool.stop(streamId);
    }

    public void setRepeatAmount(int repeatAmount) {
        this.repeatAmount = repeatAmount;
    }
}