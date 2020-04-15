package com.desiredsoftware.nurseapp;

import java.util.ArrayList;

public interface ISchedule {

    Time GetNextNotifaction(int startIndex);

    ArrayList<Time> GetNextNotifactionsByNumber(int number);

    void SetToSleepTime(Time toSleepTime);

    void SetWakeupTime(Time toSleepTime);

    void SetNotificationsPerDay(int amount);

    Time GetWakeupTime();

    Time GetToSleepTime();

    ArrayList<Time> GetScheduleList();

    int GetFirstNotificationIndex(Time deviceTime);

}
