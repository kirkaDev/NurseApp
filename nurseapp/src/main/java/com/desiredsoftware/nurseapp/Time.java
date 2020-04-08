package com.desiredsoftware.nurseapp;

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

    private void setTimeValue(int[] timeValue) {
        this.timeValue = timeValue;
    }
    
    
    // Метод получения текущего времени устройства
    static int[] getCurrentTime()
    {
        TimeZone tZone = TimeZone.getDefault();
        GregorianCalendar gregCal = new GregorianCalendar(tZone);

        return new int[] {gregCal.get(Calendar.HOUR_OF_DAY), gregCal.get(Calendar.MINUTE), gregCal.get(Calendar.SECOND)};
    }

    // Метод для вычисления временного промежутка
    Object substractTime()
    {
        // TODO: to implement
        return new Object();
    }

    // Разделить временной интервал на равномерные отрезки
    void splitByTimeIntervals()
    {

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
