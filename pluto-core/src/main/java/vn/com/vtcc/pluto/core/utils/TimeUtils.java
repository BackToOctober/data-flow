package vn.com.vtcc.pluto.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtils {

    private static Calendar calendar = GregorianCalendar.getInstance();

    public static boolean changeMinute() {
        return true;
    }

    public static boolean changeHour() {
        return true;
    }

    public static boolean changeDay() {
        return true;
    }

    public static boolean diffHour(long time1, long time2) {
        return getHour(time1) != getHour(time2);
    }

    public static int getHour(long time) {
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(long time) {
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.MINUTE);
    }

    public static String formatPattern(String pattern, long time) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(time));
    }
}
