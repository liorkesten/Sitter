//package service.sitter.models;
//
//import android.annotation.SuppressLint;
//import android.app.TimePickerDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.res.Resources;
//import android.util.Log;
//import android.widget.NumberPicker;
//import android.widget.TimePicker;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CustomTimePickerDialog extends TimePickerDialog {
//
//    private final static int TIME_PICKER_INTERVAL = 15;
//    private TimePicker mTimePicker;
//    private final OnTimeSetListener mTimeSetListener;
//
//    public CustomTimePickerDialog(Context context, OnTimeSetListener listener,
//                                  int hourOfDay, int minute, boolean is24HourView) {
//        super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay,
//                minute / TIME_PICKER_INTERVAL, is24HourView);
//        mTimeSetListener = listener;
//    }
//
//    @Override
//    public void updateTime(int hourOfDay, int minuteOfHour) {
//        mTimePicker.setCurrentHour(hourOfDay);
//        mTimePicker.setCurrentMinute(minuteOfHour / TIME_PICKER_INTERVAL);
//    }
//
//    @Override
//    public void onClick(DialogInterface dialog, int which) {
//        switch (which) {
//            case BUTTON_POSITIVE:
//                if (mTimeSetListener != null) {
//                    mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
//                            mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
//                }
//                break;
//            case BUTTON_NEGATIVE:
//                cancel();
//                break;
//        }
//    }
//
//    @SuppressLint("NewApi")
//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        try {
//            int TIME_PICKER_INTERVAL = 15;
//            NumberPicker minutePicker = (NumberPicker) mTimePicker.findViewById(Resources.getSystem().getIdentifier(
//                    "minute", "id", "android"));
//            minutePicker.setMinValue(0);
//            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
//            List<String> displayedValues = new ArrayList<String>();
//            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
//                displayedValues.add(String.format("%02d", i));
//            }
//            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));
//        } catch (Exception e) {
//            Log.e("Custom", "Exception: " + e);
//        }
//    }
//}