package org.netcook.calendar.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static void main(String[] args) {
        getFirstDayOfTheMonth(new Date());
    }

    public static Date getFirstDayOfTheMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (5 + calendar.get(Calendar.DAY_OF_WEEK)) % 7;
    }
}
