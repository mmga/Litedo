package com.mmga.litedo.util;

import com.mmga.litedo.MyApplication;
import com.mmga.litedo.R;

import java.util.Calendar;

public class DateUtil {

    private static final long MINUTE = 60 * 1000;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long WEEK = 7 * DAY;
    private static final long MONTH = 30 * DAY;



    public static String simpleTime(long timeInMillis) {
        long timeNow = System.currentTimeMillis();
        long timeInterval = timeNow - timeInMillis;
        if (timeInterval <= 0) {
            return dateString(R.string.time_error);
        } else if (timeInterval < 5 * MINUTE) {
            return dateString(R.string.justnow);
        } else if (timeInterval < HOUR) {
            return dateString(R.string.five_minutes_ago);
        } else if (timeInterval < 12 * HOUR) {
            return dateString(R.string.one_hour_ago);
        } else if (timeInterval < DAY) {
            return dateString(R.string.half_day_ago);
        } else if (timeInterval < 2 * DAY) {
            return dateString(R.string.one_day_ago);
        } else if (timeInterval < 3 * DAY) {
            return dateString(R.string.two_days_ago);
        } else if (timeInterval < 4 * DAY) {
            return dateString(R.string.three_days_ago);
        } else if (timeInterval < 5 * DAY) {
            return dateString(R.string.four_days_ago);
        } else if (timeInterval < 6 * DAY) {
            return dateString(R.string.five_days_ago);
        } else if (timeInterval < WEEK) {
            return dateString(R.string.six_days_ago);
        } else if (timeInterval < 10 * DAY) {
            return dateString(R.string.one_week_ago);
        } else if (timeInterval < 2 * WEEK) {
            return dateString(R.string.ten_days_ago);
        } else if (timeInterval < 3 * WEEK) {
            return dateString(R.string.two_weeks_ago);
        } else if (timeInterval < MONTH) {
            return dateString(R.string.three_weeks_ago);
        } else if (timeInterval < 3 * MONTH) {
            return dateString(R.string.one_month_ago);
        } else {
            return dateString(R.string.three_months_ago);
        }
    }

    private static String dateString(int resId) {
        return MyApplication.getContext().getResources().getString(resId);
    }

    private static String hourString;
    private static String minuteString;

    public static String detailedTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        hourString = ""+hour;
        if (hour < 10) {
            hourString = "0" + hour;
        }
        int minute = cal.get(Calendar.MINUTE);
        minuteString = "" + minute;
        if (minute < 10) {
            minuteString = "0" + minute;
        }
        return String.valueOf(month) + "-" + day + " " + hourString + ":" + minuteString;
    }
}
