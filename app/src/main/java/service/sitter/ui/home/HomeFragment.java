package service.sitter.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;


import service.sitter.R;
import service.sitter.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    Button btnDatePicker, btnStartTime, btnEndTime;
    EditText txtDate, txtStartTime, txtEndTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        btnDatePicker=binding.btnDateRequest;
        txtDate=binding.editTextDateRequest;

        btnStartTime=binding.btnStartTimeRequest;
        txtStartTime=binding.editTextStartTimeRequest;

        btnEndTime=binding.btnEndTimeRequest;
        txtEndTime=binding.editTextEndTimeRequest;

        Places.initialize(getActivity(), "AIzaSyBdqw1rneIi782h8_AylBcvTyYdICZY3GE");

        PlacesClient placesClient = Places.createClient(getActivity());


        btnDatePicker.setOnClickListener(v -> {
            final Calendar cal = Calendar.getInstance();
            mYear = cal.get(Calendar.YEAR);
            mMonth = cal.get(Calendar.MONTH);
            mDay = cal.get(Calendar.DAY_OF_MONTH);


            Log.d("HomeFragment", "11111111111111111");
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            Log.d("HomeFrag", txtDate.getText().toString());

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
                    });


        btnStartTime.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            TimePickerDialog tp1 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    cal.set(Calendar.MINUTE, minute);
                    txtStartTime.setText(cal.get(Calendar.HOUR_OF_DAY) + ":" + Calendar.MINUTE);
                    Log.d("HomeFrag", txtStartTime.getText().toString());
                }
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
            tp1.show();


        });
//        btnDatePicker.setOnClickListener((View.OnClickListener) this);
//        btnTimePicker.setOnClickListener((View.OnClickListener) this);

//        final TextView textView = binding.textHome;

        btnEndTime.setOnClickListener(v -> {
                    Calendar cal = Calendar.getInstance();
                    TimePickerDialog tp = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            cal.set(Calendar.MINUTE, minute);
                            txtEndTime.setText(cal.get(Calendar.HOUR_OF_DAY) + ":" + Calendar.MINUTE);
                            Log.d("HomeFrag", txtEndTime.getText().toString());
                        }
                    }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                    tp.show();
                });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                txtStartTime.setText(s);
            }
        });
//        final DatePicker datePicker = binding.datePickerRequest;
//        final TimePicker TimePicker = bi

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}