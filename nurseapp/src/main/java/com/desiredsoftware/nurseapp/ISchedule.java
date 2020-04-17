package com.desiredsoftware.nurseapp;

import java.util.ArrayList;

public interface ISchedule {


    ArrayList<Time> GetNextNotifactions(int number);

    Time GetNextNotifaction();

    void SetToSleepTime(Time toSleepTime);

    void SetWakeupTime(Time toSleepTime);

    void SetNotificationsPerDay(int amount);

    Time GetWakeupTime();

    Time GetToSleepTime();

    ArrayList<Time> GetScheduleList();

    int GetFirstNotificationIndex(Time deviceTime);

}
