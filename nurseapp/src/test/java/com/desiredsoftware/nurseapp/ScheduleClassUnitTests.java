package com.desiredsoftware.nurseapp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;

import static org.junit.Assert.assertTrue;

public class ScheduleClassUnitTests {
    @Test
    public void GetInitialSchedule()
    {
        ISchedule defaultSchedule = new Schedule();

        // Значения формируются из начальных условий, указываемых в конструкторе класса Schedule
        ArrayList<Time> defaultsPoints = defaultSchedule.GetSchedule();

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
    public void TestRefreshSchedule()
    {
        int nextPointPosition=0;
        ISchedule scheduleExample = new Schedule();

        ArrayList<Time> schedule = scheduleExample.GetSchedule();

        Time deviceTime = new Time (15,0,0);

        ListIterator<Time> listIterator = schedule.listIterator();

        while (listIterator.hasNext())
        {
            nextPointPosition++;
            if (Time.getTimeValueOnSeconds(deviceTime) > Time.getTimeValueOnSeconds(listIterator.next()))
            {
                continue;
            }
            else
            {
                break;
            }
        }

        assertTrue(nextPointPosition==3);
    }
}
