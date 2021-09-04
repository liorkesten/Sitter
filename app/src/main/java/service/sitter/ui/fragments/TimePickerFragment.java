package service.sitter.ui.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import java.util.Calendar;

import service.sitter.R;

public class TimePickerFragment  extends Fragment {


    public TimePickerFragment() {
        super(R.layout.fragment_time_picker);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        EditText editTextTime = view.findViewById(R.id.edit_text_time);

        TimeDialogButtonFragment timeDialogButtonFragment = new TimeDialogButtonFragment("SELECT START TIME", () -> {
            Calendar cal = Calendar.getInstance();
            TimePickerDialog tp = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    cal.set(Calendar.MINUTE, minute);

//                    ((Button)view.findViewById(R.id.button_time_dialog)).setText(cal.get(Calendar.HOUR_OF_DAY) + ":" + Calendar.MINUTE);
//                    editTextTime.setText(cal.get(Calendar.HOUR_OF_DAY) + ":" + Calendar.MINUTE);
//                    Log.d("HomeFrag", editTextTime.getText().toString());
                }
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
            tp.show();
            return cal.get(Calendar.HOUR_OF_DAY) + ":" + Calendar.MINUTE;
        });


        getFragmentManager().beginTransaction()
                .replace(view.findViewById(R.id.button_start_time_dialog_fragment_container_view).getId(), timeDialogButtonFragment).commit();




//        btnStartTime.setOnClickListener(v -> {
//            Calendar cal = Calendar.getInstance();
//            TimePickerDialog tp1 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                    cal.set(Calendar.MINUTE, minute);
//                    txtStartTime.setText(cal.get(Calendar.HOUR_OF_DAY) + ":" + Calendar.MINUTE);
//                    Log.d("HomeFrag", txtStartTime.getText().toString());
//                }
//            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
//            tp1.show();
//
//
//        });
////        btnDatePicker.setOnClickListener((View.OnClickListener) this);
////        btnTimePicker.setOnClickListener((View.OnClickListener) this);
    }
}
