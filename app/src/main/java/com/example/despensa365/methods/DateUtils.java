package com.example.despensa365.methods;

import com.example.despensa365.enums.Day;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date getNextWeekMonday() {
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

    public static Date getNextWeekSunday() {
        Calendar calendar = Calendar.getInstance();
        // Normalize the current date
        calendar.setTime(normalizeDate(new Date()));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // Calculate the number of days to add to reach the next Sunday
        int daysUntilNextSunday = (Calendar.SUNDAY - dayOfWeek + 7) % 7;
        // If today is Sunday or we need to get the next Sunday, hence we add 7 days
        if (daysUntilNextSunday == 0 || daysUntilNextSunday < 7) {
            daysUntilNextSunday += 7;
        }
        calendar.add(Calendar.DAY_OF_YEAR, daysUntilNextSunday);
        return normalizeDate(calendar.getTime());
    }

    public static Date normalizeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static int getDateOfWeekToday(){

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day == Calendar.SUNDAY){
            day = 7;
        }
        else{
            day--;
        }
        return day;
    }

    public static Day convertIntToDay(int value) {
        return Arrays.stream(Day.values()).filter(x -> x.getValue() == value).findFirst().orElse(Day.MONDAY);
    }

}
