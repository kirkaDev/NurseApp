package com.desiredsoftware.nurseapp;

import android.util.Log;

import java.util.Date;



public class Schedule {

    private final String MyTag = "Schedule class: ";

    enum AttributeForScheduleChange {CHANGE_BY_NOTIFICATION_PER_DAY, CHANGE_BY_SLEEPING_HOURS}

    private Time wakeupTime;

    private Time toSleepTime;

    // Текущее количество напоминаний в день
    private int currentNotificationsPerDay;

    // Предыдущее количество напоминаний в день
    private int previousNotificationsPerDay;

    public int getCurrentNotificationsPerDay() {
        return currentNotificationsPerDay;
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
        Log.d(MyTag, "Установлено новое время подъема");
        this.wakeupTime = wakeupTime;
        refreshSchedule(AttributeForScheduleChange.CHANGE_BY_SLEEPING_HOURS);
    }

    public Time getToSleepTime() {
        return toSleepTime;
    }

    public void setToSleepTime(Time toSleepTime) {
        Log.d(MyTag, "Установлено новое время засыпания");
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
                Log.d(MyTag, "Расписание будет обновлено по SLEEPING_HOURS ");
            }

            case CHANGE_BY_NOTIFICATION_PER_DAY:
            {
                // TODO: to implement full
                Log.d(MyTag, "Расписание будет обновлено по изменению количества уведомлений в день ");
            }
        }

    }

    private void splitByIntervals()
    {

    }

}

