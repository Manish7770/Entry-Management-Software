package com.example.reception;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class CommonData {

    public static String getDate(long time)
    {
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date=new StringBuilder(DateFormat.format("dd-MM-yyyy",calendar).toString());
        return date.toString();
    }
    public static String getTime(long time)
    {
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder time2=new StringBuilder(DateFormat.format("HH:mm:ss",calendar).toString());
        return time2.toString();
    }

}
