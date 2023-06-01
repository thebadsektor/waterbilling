package com.westframework.waterbillingapp.data.helpers;

import android.content.Context;
import android.util.Log;

import com.westframework.waterbillingapp.data.bill.helpers.DBHelperBill;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DateHelper {

    private static DBHelperBill dbHelperBill;

    static DateFormat inputFormat = new SimpleDateFormat("mm-dd-yyyy", Locale.US);
    static DateFormat outputFormat = new SimpleDateFormat("mm-dd-yyyy", Locale.US);
    static DateFormat outputNoDashes = new SimpleDateFormat("yyyyMMdd", Locale.US);
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    static Calendar calendar;

    public DateHelper(DBHelperBill a, Calendar b){
        this.dbHelperBill = a;
        this.calendar = b;
    }

    public static String todayNoDashes(){
        Calendar a = Calendar.getInstance();
        Date b = a.getTime();
        String date = outputNoDashes.format(b);
        return date;
    }

    public static String getPeriodCovered(String rawDate){

        Calendar c = Calendar.getInstance();
        try {
            c.setTime(inputFormat.parse(rawDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 15);
        String newDate = outputFormat.format(c.getTime());

        return rawDate + " to " + newDate;
    }

    public static String getGrandDate(ArrayList<String> holidays, String rawDueDate) throws ParseException {
        Date parsedDueDate = inputFormat.parse(rawDueDate);
        String newDate = outputFormat.format(parsedDueDate);
        String dueDate = outputFormat.format(parsedDueDate);
        formatter = formatter.withLocale(Locale.US);

        LocalDate date = LocalDate.parse(newDate, formatter);

        Calendar c = Calendar.getInstance();
        c.setTime(inputFormat.parse(rawDueDate));

        Log.d("rawDueDate", "rawDueDate: " + rawDueDate);
        Log.d("parsedDueDate", "parsedDueDate: " + parsedDueDate);
        Log.d("newDate (init)", "newDate (init): " + newDate);

        for(String holiday: holidays){
            if(holidays.contains(newDate)){
                c.add(Calendar.DATE, 1);
                newDate = outputFormat.format(c.getTime());
            }
        }

        if(isSaturday(newDate)){
            c.add(Calendar.DATE, 2);
            newDate = outputFormat.format(c.getTime());
        }

        if(isSunday(newDate)){
            c.add(Calendar.DATE, 1);
            newDate = outputFormat.format(c.getTime());
        }
        Log.d("newDate", "newDates: " + newDate);
        return newDate;
    }

    public static boolean isSaturday(String date)
    {
        final LocalDate ld = LocalDate.parse(date, formatter);
        DayOfWeek day = DayOfWeek.of(ld.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.SATURDAY;
    }

    public static boolean isSunday(String date)
    {
        final LocalDate ld = LocalDate.parse(date, formatter);
        DayOfWeek day = DayOfWeek.of(ld.get(ChronoField.DAY_OF_WEEK));
        final int day1 = Log.d("DAY", String.valueOf(ld)  + " " + String.valueOf(day));
        return day == DayOfWeek.SUNDAY;
    }

    public static String getMonth(int monthNum) {
        String month = "";

        switch (monthNum){
            case 1:
                month = "January";
                break;
            case 2:
                month = "February";
                break;
            case 3:
                month = "March";
                break;
            case 4:
                month = "April";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "June";
                break;
            case 7:
                month = "July";
                break;
            case 8:
                month = "August";
                break;
            case 9:
                month = "September";
                break;
            case 10:
                month = "October";
                break;
            case 11:
                month = "November";
                break;
            case 12:
                month = "December";
                break;
        }

        return month;
    }
}
