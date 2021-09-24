package service.sitter.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.Place;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import service.sitter.R;
import service.sitter.databinding.FragmentHomeBinding;
import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.models.Child;
import service.sitter.models.Request;
import service.sitter.recyclerview.children.ChildAdapter;
import service.sitter.ui.fragments.DateFragment;
import service.sitter.ui.fragments.LocationFragment;
import service.sitter.ui.fragments.PaymentFragment;
import service.sitter.ui.fragments.TimeFragment;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private LocalDate date = LocalDate.now();
    private String startTime = "";
    private String endTime = "";
    private int payment = 1;
    private Place location;
    private List<Child> children = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Set Logic Business Components
        IDataBase db = DataBase.getInstance();
        DateFragment dateFragment = new DateFragment();
        TimeFragment startTimeFragment = new TimeFragment("16:00", "Start Time");
        TimeFragment endTimeFragment = new TimeFragment("21:30", "End Time");
        PaymentFragment paymentFragment = new PaymentFragment();
        LocationFragment locationFragment = new LocationFragment();

//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        // Set UI Components
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button publishRequestButton = root.findViewById(R.id.publish_request_button);
        EditText descriptionEditText = root.findViewById(R.id.description_edit_text);
        RecyclerView childrenRecyclerView = root.findViewById(R.id.recycler_view_children);

        ChildAdapter childAdapter = new ChildAdapter(child -> { /*TODO Implement this listener*/});
        childrenRecyclerView.setAdapter(childAdapter);

        List<Child> children = new ArrayList<>();
        // TODO Extract children from parent.
        children.add(new Child("Daria", 1, "Daria"));
        children.add(new Child("Gali", 3, "Gali"));
        children.add(new Child("Mika", 5, "Mika"));
        childAdapter.setChildren(children);

        childrenRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        // Set default values:
        date = dateFragment.getLiveData().getValue();
        startTime = startTimeFragment.getLiveData().getValue();
        endTime = endTimeFragment.getLiveData().getValue();
        payment = paymentFragment.getLiveData().getValue();
        location = locationFragment.getLiveData().getValue();

        // Get Request Data
        dateFragment.getLiveData().observe(getViewLifecycleOwner(), newDate -> this.date = newDate);
        startTimeFragment.getLiveData().observe(getViewLifecycleOwner(), newStartTime -> this.startTime = newStartTime);
        endTimeFragment.getLiveData().observe(getViewLifecycleOwner(), newEndTime -> this.endTime = newEndTime);
        paymentFragment.getLiveData().observe(getViewLifecycleOwner(), payment -> this.payment = payment);
        locationFragment.getLiveData().observe(getViewLifecycleOwner(), location -> this.location = location);

        // Adding Request
        publishRequestButton.setOnClickListener(l -> {
            //TODO Change the publisherId to id from SP.
            Request request = new Request("111", this.date, startTime, endTime, location, null, payment, descriptionEditText.getText().toString());
            db.addRequest(request);
        });

        // Rendering Fragment
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.date_fragment_container_view, dateFragment)
                .add(R.id.start_time_fragment_container_view, startTimeFragment)
                .add(R.id.end_time_fragment_container_view, endTimeFragment)
                .add(R.id.location_fragment_container_view, locationFragment)
                .add(R.id.payment_fragment_container_view, paymentFragment)
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