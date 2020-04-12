package com.desiredsoftware.nurseapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class Schedule implements ISchedule {

    final String Log_Tag = "Schedule class: ";
    final private int initialNotificationsAmount = 8;

    private int nextTimePointPosition;

    private Time initialWakeUpTime;
    private Time initialToSleepTime;

    private int notificationsPerDay;

    private Time wakeupTime;
    private Time toSleepTime;

    private ArrayList<Time> initialSchedule;
    private ArrayList<Time> previousSchedule;
    private ArrayList<Time> schedule;

    // Предыдущее количество напоминаний в день
    private int previousNotificationsPerDay;

    Schedule()
    {
        initialWakeUpTime = new Time (9,0,0);

        initialToSleepTime = new Time (23,0,0);

        notificationsPerDay = initialNotificationsAmount;
        wakeupTime = initialWakeUpTime;
        toSleepTime = initialToSleepTime;

        initialSchedule = getInitialSchedule();
        schedule = initialSchedule;
    }

    public void setCurrentNotificationsPerDay(int newNotificationsPerDay) {
        previousNotificationsPerDay = this.notificationsPerDay;
        this.notificationsPerDay = newNotificationsPerDay;

        GetSchedule();
    }

    @Override
    public Time GetWakeupTime() {
        return wakeupTime;
    }

    @Override
    public Time GetToSleepTime() {
        return toSleepTime;
    }

    @Override
    public void SetWakeupTime(Time wakeupTime) {
        Log.d(Log_Tag, "Установлено новое время подъема");
        this.wakeupTime = wakeupTime;
        GetSchedule();
    }

    @Override
    public void SetNotificationsPerDay(int amount) {
        Log.d(Log_Tag, "Количество напоминаний в день изменено");
        this.notificationsPerDay = amount;
        GetSchedule();
    }

    @Override
    public void SetToSleepTime(Time toSleepTime) {
        Log.d(Log_Tag, "Установлено новое время засыпания");
        this.toSleepTime = toSleepTime;
        GetSchedule();
    }

    public ArrayList<Time> getInitialSchedule()
    {
        return Time.splitByTimePoints(initialNotificationsAmount, initialWakeUpTime, initialToSleepTime);
    }


    @Override
    public Time GetNextNotifaction() {
        Time nextTime;

        if (nextTimePointPosition <= schedule.size()) {
            nextTime = schedule.get(nextTimePointPosition);
            nextTimePointPosition++;
        }
        else {
            nextTimePointPosition = 0;
            nextTime = schedule.get(nextTimePointPosition);
            nextTimePointPosition++;
        }
        return nextTime;
    }

    @Override
    public int GetCurrentNotificationsPerDay() {
        return notificationsPerDay;
    }

    @Override
    public ArrayList<Time> GetSchedule() {
        return Time.splitByTimePoints(notificationsPerDay, wakeupTime, toSleepTime);
    }
}

