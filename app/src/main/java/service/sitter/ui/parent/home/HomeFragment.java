package service.sitter.ui.parent.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
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
import service.sitter.models.Babysitter;
import service.sitter.models.Child;
import service.sitter.models.Parent;
import service.sitter.models.Request;
import service.sitter.recyclerview.children.ChildAdapter;
import service.sitter.ui.fragments.DateFragment;
import service.sitter.ui.fragments.LocationFragment;
import service.sitter.ui.fragments.PaymentFragment;
import service.sitter.ui.fragments.TimeFragment;
import service.sitter.utils.SharedPreferencesUtils;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private LocalDate date = LocalDate.now();
    private String startTime = "";
    private String endTime = "";
    private int payment = 1;
    private Place location;

    private IDataBase db;
    private SharedPreferences sp;
    private Parent parent;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        // Load databases objects;
        db = DataBase.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        parent = SharedPreferencesUtils.getParentFromSP(sp);

        // Set Logic Business Components
        DateFragment dateFragment = new DateFragment();
        TimeFragment startTimeFragment = new TimeFragment("16:00", "Start Time");
        TimeFragment endTimeFragment = new TimeFragment("21:30", "End Time");
        PaymentFragment paymentFragment = new PaymentFragment(parent.getDefaultPricePerHour());
        LocationFragment locationFragment = new LocationFragment();


        // Set UI Components
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button publishRequestButton = root.findViewById(R.id.publish_request_button);
        EditText descriptionEditText = root.findViewById(R.id.description_edit_text);
        RecyclerView childrenRecyclerView = root.findViewById(R.id.recycler_view_children);

        ChildAdapter childAdapter = new ChildAdapter(child -> { /*TODO Implement this listener*/});
        childrenRecyclerView.setAdapter(childAdapter);
        childAdapter.setChildren(parent.getChildren());

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
            String description = descriptionEditText.getText().toString();
            Request request = new Request(
                    parent.getUuid(), this.date, startTime, endTime, location,
                    null, payment, description
            );
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


    private void funcShouldBeDeletedOnceSpIsReadyInitMyUser() {
        IDataBase db = DataBase.getInstance();
        Babysitter b = new Babysitter("Noam", "Kesten", "n@gma", "0547718646", "NY", "URL_TO_IMAGE");
        b.setUuid("123");
        db.addBabysitter(b);
        // First step - create new user with default values;
        List<Child> children = new ArrayList<>();
        children.add(new Child("Daria", "10/10/2020", "Daria"));
        children.add(new Child("Gali", "10/10/2018", "Gali"));
        children.add(new Child("Mika", "10/10/2015", "Mika"));
        Parent myParent = new Parent("Lior", "Kesten", "kestenlior@gmail.com", "+972547718647", "NY", "<URL_TO_IMAGE>", children, 60);
        // Step 2: add the user to the db.
        db.addParent(myParent);

        // Step 3: save parent in SP.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferencesUtils.saveParentToSP(sp, myParent);
//         Step 4: Extract from SP.
        parent = SharedPreferencesUtils.getParentFromSP(sp);
        Log.d(TAG, String.format("myUser initialized: <%s>", parent.toString()));
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