package com.eric.mybill.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTransform {

    private static final Date sDate = new Date(System.currentTimeMillis());
    private static final int[] sYMD = fromDate(sDate);

    private static SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sFormat2 = new SimpleDateFormat("MM-dd");

    public static int[] fromDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int result[] = {year, month + 1, day};
        return result;
    }


    public static String stringFromDate(Date date){
        return sFormat.format(date);
    }

    public static String stringFromDate2(Date date){
        return sFormat2.format(date);
    }

    public static Date toDate(int year, int month, int day) throws ParseException {
        return sFormat.parse(year + "-" + month + "-" + day);
    }

    public static int getThisYear(){
        return sYMD[0];
    }

    public static int getThisMonth(){
        return sYMD[1];
    }
}
