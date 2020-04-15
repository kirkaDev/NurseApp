package com.desiredsoftware.nurseapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.ListIterator;

public class Schedule implements ISchedule {

    final String Log_Tag = "Schedule class: ";

    private int nextTimePointPosition;

    private int notificationsPerDay;

    private Time wakeupTime;
    private Time toSleepTime;

    private ArrayList<Time> scheduleList;

    Schedule(int initialNotificationsNumber, Time initialWakeupTime, Time initialToSleepTime)
    {
        this.notificationsPerDay = initialNotificationsNumber;
        this.wakeupTime = initialWakeupTime;
        this.toSleepTime = initialToSleepTime;

        scheduleList = GetScheduleList();
    }

    @Override
    public ArrayList<Time> GetScheduleList() {
        return Time.splitByTimePoints(notificationsPerDay, wakeupTime, toSleepTime);
    }


    // Здесь, в зависимости от времени девайса на момент поиска первой точки будет срабатывать
    // один из 5 сценариев
    @Override
    public int GetFirstNotificationIndex(Time deviceTime) {

        int devTimeOnSec = Time.getTimeValueOnSeconds(deviceTime);
        int wakeupTimeOnSec = Time.getTimeValueOnSeconds(this.wakeupTime);
        int toSleepTimeOnSec = Time.getTimeValueOnSeconds(this.toSleepTime);

        // (1) Когда все точки за день пройдены и человек должен по идее спать,
        // первая точка в его расписании будет с подъемом
        // (8:00 - 01:00; deviceTime = 7:00)
        if (devTimeOnSec < wakeupTimeOnSec && devTimeOnSec > toSleepTimeOnSec)
        {
            return 0;
        }

        // (2) (8:00 - 22:00; deviceTime = 7:00)
        if (devTimeOnSec < wakeupTimeOnSec && devTimeOnSec < toSleepTimeOnSec && wakeupTimeOnSec < toSleepTimeOnSec)
        {
               return 0;
        }

        /* (3) Самый распространенный случай - когда уже одна или несколько точек
        остались позади ("пропущены") на момент выставления настроек
        (8:00 - 22:00; deviceTime = 10:00)*/
        if (devTimeOnSec > wakeupTimeOnSec && devTimeOnSec < toSleepTimeOnSec)
        {
            int firstNotificationsIndex=0;
            ListIterator<Time> listIterator = scheduleList.listIterator();

            while (listIterator.hasNext())
            {
                Time currentItem = listIterator.next();

                if (devTimeOnSec > Time.getTimeValueOnSeconds(currentItem))
                {
                    firstNotificationsIndex++;
                    continue;
                }
                else
                {
                    break;
                }
            }
            return firstNotificationsIndex;
        }

        // (4) (8:00 - 22:00; deviceTime = 23:00)
        if ((devTimeOnSec > wakeupTimeOnSec) && (devTimeOnSec > toSleepTimeOnSec) && (wakeupTimeOnSec<toSleepTimeOnSec))
        {
            return 0;
        }

        // (5) (8:00 - 03:00; deviceTime = 23:00)
        if ((devTimeOnSec > wakeupTimeOnSec) && (devTimeOnSec > toSleepTimeOnSec) && (wakeupTimeOnSec>toSleepTimeOnSec))
        {
            int firstNotificationsIndex=0;

            /*Данный массив будет содержать все те же временные точки, что и коллекция экземпляра класса Schedule,
            только точки, переходящие на следующий день будут выглядеть с прибавлением 24 часов (86400 сек)
            (например, точка 01:00 будет представлена, как 25:00 ) - для дальнейшего сравнения с временной
            точкой, полученной с девайса и дальнейшего определения начального индекса */
            int[] afterMidnightArray = new int[scheduleList.size()];

            final int SECONDS_IN_THE_DAY = 86400;

            for (int i=0; i<afterMidnightArray.length; i++)
            {
                afterMidnightArray[i] = Time.getTimeValueOnSeconds(scheduleList.get(i));
            }

            for (int i=0; i<afterMidnightArray.length-1; i++)
            {
                if (afterMidnightArray[i]>=afterMidnightArray[i+1])
                {
                    afterMidnightArray[i+1] += SECONDS_IN_THE_DAY;
                }
            }

            for (int i=0; i<afterMidnightArray.length; i++)
            {
                if (devTimeOnSec > afterMidnightArray[i])
                {
                    firstNotificationsIndex++;
                    continue;
                }
                else
                {
                    break;
                }
            }

            return firstNotificationsIndex;
        }

        Log.d (Log_Tag, "Непредвиденное условие для поиска первой точки раписания");
              return 0;
        }

    @Override
    public Time GetWakeupTime() {
        return wakeupTime;
    }

    @Override
    public Time GetToSleepTime() {
        return toSleepTime;
    }

    @Override
    public void SetWakeupTime(Time wakeupTime) {
        Log.d(Log_Tag, "Установлено новое время подъема");
        this.wakeupTime = wakeupTime;
        scheduleList = GetScheduleList();
    }

    @Override
    public void SetNotificationsPerDay(int amount) {
        Log.d(Log_Tag, "Количество напоминаний в день изменено");
        this.notificationsPerDay = amount;
        scheduleList = GetScheduleList();
    }

    @Override
    public void SetToSleepTime(Time toSleepTime) {
        Log.d(Log_Tag, "Установлено новое время засыпания");
        this.toSleepTime = toSleepTime;
        scheduleList = GetScheduleList();
    }

    @Override
    public Time GetNextNotifaction(int startIndex) {
        // TODO: to implement
        return null;
    }

    @Override
    public ArrayList<Time> GetNextNotifactionsByNumber(int number)
    {
        ArrayList<Time> arrNextNotifications = new ArrayList<>();
        int currentIndex = GetFirstNotificationIndex(Time.getCurrentTime());

        arrNextNotifications.add(scheduleList.get(currentIndex));

        for (int i=0; i<number-1; i++)
        {
            if (currentIndex+1<=scheduleList.size()-1) {
                arrNextNotifications.add(scheduleList.get(currentIndex + 1));
                currentIndex++;
            }
            else {
                currentIndex = 0;
                arrNextNotifications.add(scheduleList.get(currentIndex));
                //currentIndex++;
            }
        }

        return arrNextNotifications;
    }
}

