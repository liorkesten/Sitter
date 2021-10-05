package service.sitter.providers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import service.sitter.ui.parent.manageRequests.ManageRequestsFragment;

public class CalendarProvider {

    private static final String TAG = CalendarProvider.class.getSimpleName();


    public static void AddCalendarEvent(Context context, String startTime, String endTime, String dateString) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(String.valueOf(DateTimeFormatter.ISO_LOCAL_DATE));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);
        Date dateStart = null;
        Date dateEnd = null;
        try {
            Log.d(TAG, dateString + " " + startTime);
            dateStart = sdf.parse(dateString + " " + startTime);
            dateEnd = sdf.parse(dateString + " " + endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeInMillisecondsStart = dateStart.getTime();
        long timeInMillisecondsEnd = dateEnd.getTime();
//        LocalDateTime localDate = LocalDateTime.parse(date, formatter);
//        int year = Integer.parseInt(date.split("/")[0]);
//        int month = Integer.parseInt(date.split("/")[1]);
//        int day = Integer.parseInt(date.split("/")[2]);
//        int startTimeInt = Integer.parseInt(startTime);
//        long timeInMilliseconds = LocalDateTime.of(year, month, day, startTimeInt);
//        long timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        Log.d(TAG, "" + timeInMillisecondsStart);
        Intent i = new Intent(Intent.ACTION_EDIT);
        i.setType("vnd.android.cursor.item/event");
        i.putExtra("beginTime", timeInMillisecondsStart);
//        i.putExtra("allDay", false);
//        i.putExtra("rule", "FREQ=YEARLY");
        i.putExtra("endTime", timeInMillisecondsEnd);
        i.putExtra("title", "Babysitting Event");
        context.startActivity(i);
    }
}
