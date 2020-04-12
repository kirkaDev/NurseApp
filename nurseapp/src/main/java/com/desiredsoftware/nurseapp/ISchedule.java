package com.desiredsoftware.nurseapp;

import java.util.ArrayList;

public interface ISchedule {

    Time GetNextNotifaction();

    int GetCurrentNotificationsPerDay();

    void SetToSleepTime(Time toSleepTime);

    void SetWakeupTime(Time toSleepTime);

    void SetNotificationsPerDay(int amount);

    Time GetWakeupTime();

    Time GetToSleepTime();

    ArrayList<Time> GetSchedule();

}
