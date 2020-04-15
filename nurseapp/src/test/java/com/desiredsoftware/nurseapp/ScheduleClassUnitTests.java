package com.desiredsoftware.nurseapp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import static org.junit.Assert.assertTrue;

public class ScheduleClassUnitTests {
    @Test
    public void GetInitialSchedule()
    {
        ISchedule defaultSchedule = new Schedule(4, new Time (10,0,0), new Time (0,0,0));

        // Значения формируются из начальных условий, указываемых в конструкторе класса Schedule
        ArrayList<Time> defaultsPoints = defaultSchedule.GetScheduleList();

        int count=0;
        boolean isVerified = false;

        Time[] arrToVerify = new Time[] {
                new Time(10, 0, 0),
                new Time(14, 40, 0),
                new Time (19,20,0),
                new Time (0,0,0),
        };

        for (Time tPointArrList: defaultsPoints)
        {
            if (Arrays.equals(tPointArrList.getTimeValue(), arrToVerify[count].getTimeValue()))
            {
                isVerified = true;
                count++;
            }
            else
            {
                isVerified = false;
                break;
            }
        }
        assertTrue(isVerified);
    }

    @Test
    public void TestFirstPointIndex1()
    {
        ISchedule schedule = new Schedule(5, new Time(8,0,0), new Time(01,0,0));

        ArrayList<Time> scheduleList = schedule.GetScheduleList();

        Time deviceTime = new Time (7,01,20);

        int firstPointIndex = schedule.GetFirstNotificationIndex(deviceTime);

        assertTrue(firstPointIndex==0);
    }

    @Test
    public void TestFirstPointIndex2()
    {
        ISchedule schedule = new Schedule(5, new Time(8,30,4), new Time(22,5,7));

        ArrayList<Time> scheduleList = schedule.GetScheduleList();

        Time deviceTime = new Time (7,01,20);

        int firstPointIndex = schedule.GetFirstNotificationIndex(deviceTime);

        assertTrue(firstPointIndex==0);
    }

    @Test
    public void TestFirstPointIndex3()
    {
        ISchedule schedule = new Schedule(10, new Time(8,30,4), new Time(23,5,7));

        ArrayList<Time> scheduleList = schedule.GetScheduleList();

        Time deviceTime = new Time (11,02,43);

        int firstPointIndex = schedule.GetFirstNotificationIndex(deviceTime);

        assertTrue(firstPointIndex==2);
    }

    @Test
    public void TestFirstPointIndex4()
    {
        ISchedule schedule = new Schedule(10, new Time(8,30,4), new Time(23,5,7));

        ArrayList<Time> scheduleList = schedule.GetScheduleList();

        Time deviceTime = new Time (23,15,43);

        int firstPointIndex = schedule.GetFirstNotificationIndex(deviceTime);

        assertTrue(firstPointIndex==0);
    }

    @Test
    public void TestFirstPointIndex5()
    {
        ISchedule schedule = new Schedule(6, new Time(8,30,4), new Time(04,5,7));

        ArrayList<Time> scheduleList = schedule.GetScheduleList();

        Time deviceTime = new Time (23,15,43);

        int firstPointIndex = schedule.GetFirstNotificationIndex(deviceTime);

        assertTrue(firstPointIndex==4);
    }
}
