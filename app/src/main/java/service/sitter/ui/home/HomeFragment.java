package service.sitter.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;


import service.sitter.R;
import service.sitter.databinding.FragmentHomeBinding;
import service.sitter.ui.fragments.TimeButtonFragment;
import service.sitter.ui.fragments.TimeFragment;

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
//        ScrollView scrollView = (ScrollView) root.findViewById(R.id.scroll_view);
//        LinearLayout linearLayout = (LinearLayout) scrollView.findViewById(R.id.nested_linear_layout);

//        if(linearLayout.getParent() != null) {
//            ((ViewGroup)linearLayout.getParent()).removeView(linearLayout); // <- fix
//        }


//        MutableLiveData<String> startTimeLiveData = new LiveData<String>();
        TimeFragment startTimeFragment = new TimeFragment();
//        Log.d("HomeFragment", )
        TimeFragment endTimeFragment = new TimeFragment();



            getChildFragmentManager()
                .beginTransaction()
                .add(R.id.start_time_fragment_container_view, startTimeFragment)
                .add(R.id.end_time_fragment_container_view, endTimeFragment)
                .commit();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


//        btnDatePicker=binding.btnDateRequest;
//        txtDate=binding.editTextDateRequest;

//        btnStartTime=binding.btnStartTimeRequest;
//        txtStartTime=binding.editTextStartTimeRequest;
//
//        btnEndTime=binding.btnEndTimeRequest;
//        txtEndTime=binding.editTextEndTimeRequest;

//        Places.initialize(getActivity(), "AIzaSyBdqw1rneIi782h8_AylBcvTyYdICZY3GE");

//        PlacesClient placesClient = Places.createClient(getActivity());

//btnDatePicker.setOnClickListener(v -> {
//            final Calendar cal = Calendar.getInstance();
//            mYear = cal.get(Calendar.YEAR);
//            mMonth = cal.get(Calendar.MONTH);
//            mDay = cal.get(Calendar.DAY_OF_MONTH);
//
//
//            Log.d("HomeFragment", "11111111111111111");
//            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
//                    new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year,
//                                              int monthOfYear, int dayOfMonth) {
//                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                            Log.d("HomeFrag", txtDate.getText().toString());
//
//                        }
//                    }, mYear, mMonth, mDay);
//            datePickerDialog.show();
//                    });


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
//
////        final TextView textView = binding.textHome;
//
//        btnEndTime.setOnClickListener(v -> {
//                    Calendar cal = Calendar.getInstance();
//                    TimePickerDialog tp = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//                        @Override
//                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                            cal.set(Calendar.MINUTE, minute);
//                            txtEndTime.setText(cal.get(Calendar.HOUR_OF_DAY) + ":" + Calendar.MINUTE);
//                            Log.d("HomeFrag", txtEndTime.getText().toString());
//                        }
//                    }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
//                    tp.show();
//                });

//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                txtStartTime.setText(s);
//            }
//        });
//        final DatePicker datePicker = binding.datePickerRequest;
//        final TimePicker TimePicker = bi