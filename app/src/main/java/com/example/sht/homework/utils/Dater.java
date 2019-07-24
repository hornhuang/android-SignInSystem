package com.example.sht.homework.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dater {

    private static SimpleDateFormat format;

    public static int getDiscrepantDays(Date dateStart, Date dateEnd) {
        return (int) ((dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24);
    }

    public static String getYMDString(Date date){
        format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getYMRHMSString(Date date){
        format = new SimpleDateFormat("yyyy-MM-dd-HH-MM-SS");
        return format.format(date);
    }

    /*
     * 获得当前日期
     */
    public static int getWeekOfDate() {
        Date dt = new Date();
        int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}
