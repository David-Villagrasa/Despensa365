package com.example.despensa365.methods;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date getNextMonday() {
        Calendar calendar = Calendar.getInstance();
        // Normalize the current date
        calendar.setTime(normalizeDate(new Date()));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // Calculate the number of days to add to reach the next Monday
        int daysUntilNextMonday = (Calendar.MONDAY - dayOfWeek + 7) % 7;
        // If today is Monday, we need to get the next Monday, hence we add 7 days
        if (daysUntilNextMonday == 0) {
            daysUntilNextMonday = 7;
        }
        calendar.add(Calendar.DAY_OF_YEAR, daysUntilNextMonday);
        return normalizeDate(calendar.getTime());
    }

    public static Date getNextSunday() {
        Calendar calendar = Calendar.getInstance();
        // Normalize the current date
        calendar.setTime(normalizeDate(new Date()));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // Calculate the number of days to add to reach the next Sunday
        int daysUntilNextSunday = (Calendar.SUNDAY - dayOfWeek + 7) % 7;
        // If today is Sunday, we need to get the next Sunday, hence we add 7 days
        if (daysUntilNextSunday == 0) {
            daysUntilNextSunday = 7;
        }
        calendar.add(Calendar.DAY_OF_YEAR, daysUntilNextSunday);
        return normalizeDate(calendar.getTime());
    }

    private static Date normalizeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
