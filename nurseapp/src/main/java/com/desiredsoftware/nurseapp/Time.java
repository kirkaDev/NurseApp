package com.desiredsoftware.nurseapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Time {


    Time()
    {

    }

    Time(int[] time) throws IllegalArgumentException
    {
            if (time[0]>=0 && time[0]<=23)
            { this.hoursNumber = time[0];}

            else
            { throw new IllegalArgumentException("Количество часов должно находиться в диапазоне от 0 до 23, принято: " + time[0]);  }

        this.minutesNumber = time[1];
        this.secondsNumber = time[2];
        setTimeValue(time);
    }

    public Time (int hours, int minutes, int seconds) throws IllegalArgumentException
    {
        if (hours>=0 && hours<=23)
        { this.hoursNumber = hours;}

        else
        { throw new IllegalArgumentException("Количество часов должно находиться в диапазоне от 0 до 23, принято: " + hours);  }

        this.minutesNumber = minutes;
        this.secondsNumber = seconds;

        setTimeValue(new int[] {hours, minutes, seconds});
    }

    String Log_TAG = "Class Time: ";

    private int hoursNumber;
    private int minutesNumber;
    private int secondsNumber;

    private int[] timeValue;

    int[] getTimeValue() {
        return timeValue;
    }

    public static int getTimeValueOnSeconds(Time timeToConvert)
    {
        return timeToConvert.hoursNumber*3600 + timeToConvert.minutesNumber*60
                + timeToConvert.secondsNumber;
    }

    // HMS - hours/munites/seconds
    static Time getTimeOnHMS (int valueOnSeconds)
    {
        int hoursToReturn;
        int minutesToReturn;
        int secondsToReturn;

        if (valueOnSeconds < 86400) {
            hoursToReturn = valueOnSeconds / 3600;
        }
        else
        {
            hoursToReturn = valueOnSeconds/3600 - 24;
        }
            minutesToReturn = (valueOnSeconds - (valueOnSeconds/3600 * 3600)) / 60;
            secondsToReturn = valueOnSeconds - (valueOnSeconds/3600 * 3600) - (minutesToReturn * 60);
            return new Time(hoursToReturn, minutesToReturn, secondsToReturn);
        }

    private void setTimeValue(int[] timeValue) {
        this.timeValue = timeValue;
    }
    
    
    // Метод получения текущего времени устройства
    static Time getCurrentTime()
    {
        TimeZone tZone = TimeZone.getDefault();
        GregorianCalendar gregCal = new GregorianCalendar(tZone);

        return new Time (gregCal.get(Calendar.HOUR_OF_DAY), gregCal.get(Calendar.MINUTE), gregCal.get(Calendar.SECOND));
    }

    public Time addTime (Time startingTimePoint, Time addedTime)
    {
        int additionResult = getTimeValueOnSeconds(startingTimePoint) + addedTime.getTimeValueOnSeconds(addedTime);
        return getTimeOnHMS(additionResult);
    }

    static public Time addTime (Time startingTimePoint, int addedTimeOnSeconds)
    {
        int additionResult = getTimeValueOnSeconds(startingTimePoint) + addedTimeOnSeconds;
        return getTimeOnHMS(additionResult);
    }

    // Разделить временной интервал на одинаковые отрезки
    static ArrayList<Time> splitByTimePoints(int segmentsNumber, Time begin, Time end) throws IllegalArgumentException
    {
        if (segmentsNumber<2)
            throw new IllegalArgumentException ("Количество напоминаний не должно быть менее 2. Принято: " + segmentsNumber);

            Time tInterval = GetTimeInterval(begin, end);
            int tIntervalOnSeconds = tInterval.getTimeValueOnSeconds(tInterval);
            int segmentOnSeconds = tIntervalOnSeconds/(segmentsNumber-1);

        ArrayList<Time> timePoints = new ArrayList<>();

        timePoints.add(begin);

        Time currentPoint = begin;
        Time nextPoint;

        Time t = addTime(begin, segmentOnSeconds);

        for (int i=0; i<segmentsNumber-2; i++)
        {
            nextPoint = addTime(currentPoint, segmentOnSeconds);
            timePoints.add(nextPoint);
            currentPoint = nextPoint;
        }

        timePoints.add(end);

        return timePoints;
    }

    static Time GetTimeInterval(Time beginningTimeInterval, Time endTimeInterval)
    {
        Time timeInterval;
        int substractionResultOnSeconds;

        int beginningTimeOnSeconds =    beginningTimeInterval.hoursNumber*3600+
                                beginningTimeInterval.minutesNumber*60+
                                beginningTimeInterval.secondsNumber;

        int endTimeOnSeconds = endTimeInterval.hoursNumber*3600+
                            endTimeInterval.minutesNumber*60+
                            endTimeInterval.secondsNumber;

        // For example, 8:00 - 23:00
        if (endTimeOnSeconds>beginningTimeOnSeconds)
        {
            substractionResultOnSeconds = endTimeOnSeconds-beginningTimeOnSeconds;

            int hoursToReturn = substractionResultOnSeconds/3600;
            int minutesToReturn = (substractionResultOnSeconds-(hoursToReturn*3600))/60;
            int secondsToReturn = substractionResultOnSeconds - (hoursToReturn*3600) - (minutesToReturn*60);
            return new Time (new int[] {hoursToReturn, minutesToReturn, secondsToReturn});
        }

        // For example, 10:00 - 01:00
        else
        {
            int secondsToMidnight = (24*3600)-beginningTimeOnSeconds;
            substractionResultOnSeconds = secondsToMidnight + endTimeOnSeconds;

            int hoursToReturn = substractionResultOnSeconds/3600;
            int minutesToReturn = (substractionResultOnSeconds-(hoursToReturn*3600))/60;
            int secondsToReturn = substractionResultOnSeconds - (hoursToReturn*3600) - (minutesToReturn*60);
            return new Time (new int[] {hoursToReturn, minutesToReturn, secondsToReturn});
        }

    }
}
