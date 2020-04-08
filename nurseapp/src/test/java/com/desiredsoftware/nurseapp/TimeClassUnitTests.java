package com.desiredsoftware.nurseapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TimeClassUnitTests {

    @Test
    public void setTimeValueVerify()
    {
        int[] timeToSet = new int[] {22,5,7};

        Time timeExmpl = new Time(timeToSet);

        int[] timeValue = timeExmpl.getTimeValue();

        assertTrue(timeValue[0]==timeToSet[0]&& timeValue[1]==
                timeToSet[1]&& timeValue[2]==timeToSet[2]);
    }

    @Test
    public void TimeIntervalCalculate1()
    {
        int[] hoursBegin = new int[] {8,30,5};
        int[] hoursEnd = new int[] {22,0,0,};

        int[] expectedResult = new int[] {13, 29, 55};

        Time begin = new Time (hoursBegin);
        Time end = new Time (hoursEnd);

        Time timeInterval = Time.GetTimeInterval(begin, end);
        int[] timeIntervalValues = timeInterval.getTimeValue();

        assertTrue((timeIntervalValues[0]==expectedResult[0])&&
                            (timeIntervalValues[1]==expectedResult[1]&&
                            timeIntervalValues[2]==expectedResult[2]));
    }

    @Test
    public void TimeIntervalCalculate2()
    {
        int[] hoursBegin = new int[] {10,5,3};
        int[] hoursEnd = new int[] {1,0,4,};

        int[] expectedResult = new int[] {14, 55, 1};

        Time begin = new Time (hoursBegin);
        Time end = new Time (hoursEnd);

        Time timeInterval = Time.GetTimeInterval(begin, end);
        int[] timeIntervalValues = timeInterval.getTimeValue();

        assertTrue((timeIntervalValues[0]==expectedResult[0])&&
                (timeIntervalValues[1]==expectedResult[1]&&
                        timeIntervalValues[2]==expectedResult[2]));
    }
}

