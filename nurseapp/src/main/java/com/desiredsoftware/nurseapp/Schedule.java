package com.desiredsoftware.nurseapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class Schedule implements ISchedule {

    final String Log_Tag = "Schedule class: ";
    final private int initialNotificationsAmount;

    enum AttributeForScheduleChange {CHANGE_BY_NOTIFICATION_PER_DAY, CHANGE_BY_SLEEPING_HOURS}

    private int nextTimePointPosition;

    private Time initialWakeUpTime;
    private Time initialToSleepTime;

    private Time currentWakeUpTime;
    private Time currentToSleepTime;
    private int currentNotificationsPerDay;

    private Time wakeupTime;
    private Time toSleepTime;

    private ArrayList<Time> initialSchedule;
    private ArrayList<Time> previousSchedule;
    private ArrayList<Time> schedule;

    // Предыдущее количество напоминаний в день
    private int previousNotificationsPerDay;

    Schedule()
    {
        initialNotificationsAmount = 4;
        initialWakeUpTime = new Time (10,0,0);

        initialToSleepTime = new Time (0,0,0);

        currentNotificationsPerDay = initialNotificationsAmount;
        currentWakeUpTime = initialWakeUpTime;
        currentToSleepTime = initialToSleepTime;

        initialSchedule = getInitialSchedule();
        schedule = initialSchedule;
    }

    public void setCurrentNotificationsPerDay(int newNotificationsPerDay) {
        previousNotificationsPerDay = this.currentNotificationsPerDay;
        this.currentNotificationsPerDay = newNotificationsPerDay;

        refreshSchedule(AttributeForScheduleChange.CHANGE_BY_NOTIFICATION_PER_DAY);
    }



    public Time getWakeupTime() {
        return wakeupTime;
    }

    public void setWakeupTime(Time wakeupTime) {
        Log.d(Log_Tag, "Установлено новое время подъема");
        this.wakeupTime = wakeupTime;
        refreshSchedule(AttributeForScheduleChange.CHANGE_BY_SLEEPING_HOURS);
    }

    public Time getToSleepTime() {
        return toSleepTime;
    }

    public void setToSleepTime(Time toSleepTime) {
        Log.d(Log_Tag, "Установлено новое время засыпания");
        this.toSleepTime = toSleepTime;
        refreshSchedule(AttributeForScheduleChange.CHANGE_BY_SLEEPING_HOURS);
    }


    private void refreshSchedule(AttributeForScheduleChange attribut)
    {
        // TODO: to implement full
        // Определить произошедшие изменения в настройках, обновить информацию об уведомлениях

        switch (attribut)
        {
            case CHANGE_BY_SLEEPING_HOURS:
            {
                // TODO: to implement full
                Log.d(Log_Tag, "Расписание будет обновлено по SLEEPING_HOURS ");
                schedule = GetSchedule();
            }

            case CHANGE_BY_NOTIFICATION_PER_DAY:
            {
                // TODO: to implement full
                previousSchedule = schedule;

                schedule = GetSchedule();



                Time deviceTime = Time.getCurrentTime();

                ListIterator<Time> listIterator = schedule.listIterator();

                for (int i=0; i<schedule.size(); i++)
                {
                    if (Time.getTimeValueOnSeconds(deviceTime) <= Time.getTimeValueOnSeconds(schedule.get(i)))
                    {
                        nextTimePointPosition = i;
                        break;
                    }
                }
            }

                Log.d(Log_Tag, "Расписание будет обновлено по изменению количества уведомлений в день ");
        }
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
        return currentNotificationsPerDay;
    }

    @Override
    public ArrayList<Time> GetSchedule() {
        return Time.splitByTimePoints(currentNotificationsPerDay, currentWakeUpTime, currentToSleepTime);
    }


}

