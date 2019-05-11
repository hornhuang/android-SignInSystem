package com.example.sht.homework.utils;

import java.util.Calendar;
import java.util.Date;

public class MyDate {

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
