package com.desiredsoftware.nurseapp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static com.desiredsoftware.nurseapp.Time.getTimeOnHMS;
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

    @Test
    public void GetTimeOnHMS()
    {
        // 8h:31min:21sec
        final int timeValueOnSeconds = 30681;

        Time timeOnHMS = Time.getTimeOnHMS(timeValueOnSeconds);

        int timeValue[] = timeOnHMS.getTimeValue();

        assertTrue(timeValue[0]==8&&timeValue[1]==31&&timeValue[2]==21);
    }

    @Test
    public void AddTime()
    {
        Time t = new Time (0,0,0);
        t = t.addTime(new Time(9,3,40), new Time (2,1,30));

        int[] values = t.getTimeValue();
        assertTrue(values[0]==11 && values[1]==5 && values[2]==10);
    }

    @Test
    public void SplitByTimePointsSimple()
    {
        int count=0;
        ArrayList<Time> timePointsArr =  Time.splitByTimePoints(6, new Time (8,0,0), new Time (10,6,0));

        boolean isVerified = false;
        Time[] arrToVerify = new Time[] {
                new Time(8, 0, 0),
                new Time(8, 25, 12),
                new Time (8,50,24),
                new Time (9,15,36),
                new Time (9,40,48),
                new Time (10,6,0)
    };
        for (Time tPointArrList: timePointsArr)
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
    public void SplitByTimePointsAfterMidnight()
    {
        {
            int count=0;
            boolean isVerified = false;

            ArrayList<Time> timePointsArr =  Time.splitByTimePoints(6, new Time (22,0,0), new Time (3,5,20));

            Time[] arrToVerify = new Time[] {
                    new Time(22, 0, 0),
                    new Time(23, 1, 4),
                    new Time (0,2,8),
                    new Time (1,3,12),
                    new Time (2,4,16),
                    new Time (3,5,20)
            };

            for (Time tPointArrList: timePointsArr)
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
    }

    @Test
    public void fromStringToTime()
    {
        String source = "9:01";

        Time t = Time.fromStringToTime(source);

        int hours = t.getTimeValue()[0];
        int minutes = t.getTimeValue()[1];
        int seconds = t.getTimeValue()[2];

        assertTrue(hours==9 && minutes == 1 && seconds == 0);

    }
}

