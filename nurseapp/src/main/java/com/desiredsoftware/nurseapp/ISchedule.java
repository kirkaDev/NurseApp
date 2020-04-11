package com.desiredsoftware.nurseapp;

import java.util.ArrayList;

public interface ISchedule {

    Time GetNextNotifaction();

    int GetCurrentNotificationsPerDay();

    ArrayList<Time> GetSchedule();

}
