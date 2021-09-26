package service.sitter.ui.parent.home;

import static java.lang.System.exit;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
import service.sitter.db.DataBaseUtils;
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
    private Parent myUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        funcShouldBeDeletedOnceSpIsReadyInitMyUser();


        // Set UI Components
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button publishRequestButton = root.findViewById(R.id.publish_request_button);
        EditText descriptionEditText = root.findViewById(R.id.description_edit_text);
        RecyclerView childrenRecyclerView = root.findViewById(R.id.recycler_view_children);


        // Set Logic Components
        IDataBase db = DataBase.getInstance();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());
//        SharedPreferencesUtils.saveParentToSP(sp, myParent);
//         Step 4: Extract from SP.
        myUser = SharedPreferencesUtils.getParentFromSP(sp);

        //Extract user from SP.
        // TODO Don't delete the comments below - it will be used once SP will be saved after SetProfile.
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());
//        String userUID = sp.getString("userUID", "");
//        String userType = sp.getString("userType", "");
//        if (userUID.equals("") || userType.equals("")) {
//            //TODO ERROR!!!!
//            Log.e(TAG, String.format("Error to extract userUID or userType from sp.\nuserUID: <%s>\nuserType:<%s", userUID, userType));
//            exit(11);
//        }
//        // Verify that the user type is equal to the type in the SP.
//        try {
//            myUser = db.getParent(userUID);
//        } catch (UserNotFoundException e) {
//            //TODO ERROR!!!!
//            Log.e(TAG, String.format("Error to extract userUID from DB\nuserUID: <%s>", userUID));
//            exit(12);
//        }
        // TODO Delete the code below - it will be used once SP will be saved after SetProfile.
//        funcShouldBeDeletedOnceSpIsReadyInitMyUser();
        if (myUser == null) {
            //TODO handle exit
            exit(101);
        }

        ImageButton viewById = root.findViewById(R.id.profile_picture_image_button);
        DataBaseUtils.loadImage(myUser.getImage(), viewById::setImageURI);

        ChildAdapter childAdapter = new ChildAdapter(child -> { /*TODO Implement this listener*/});
        childrenRecyclerView.setAdapter(childAdapter);
        childAdapter.setChildren(myUser.getChildren());
        // TODO Delete until here

        // Set Logic Business Components
        DateFragment dateFragment = new DateFragment();
        TimeFragment startTimeFragment = new TimeFragment("16:00", "Start Time");
        TimeFragment endTimeFragment = new TimeFragment("21:30", "End Time");
        PaymentFragment paymentFragment = new PaymentFragment(myUser.getDefaultPricePerHour());
        LocationFragment locationFragment = new LocationFragment();

//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);


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
                    myUser.getUuid(), this.date, startTime, endTime, location,
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
        Babysitter b = new Babysitter("Noam", "Kesten", "n@gma", "0547718646", "NY", "URL_TO_IMAGE", false);
        b.setUuid("123");
        db.addBabysitter(b);
        // First step - create new user with default values;
        List<Child> children = new ArrayList<>();
//        children.add(new Child("Daria", "10/10/2020", ));
//        children.add(new Child("Gali", "10/10/2018", "Gali"));
//        children.add(new Child("Mika", "10/10/2015", "Mika"));
        Parent myParent = new Parent("Lior", "Kesten", "kestenlior@gmail.com", "+972547718647", "NY", "<URL_TO_IMAGE>", children, 60);
        // Step 2: add the user to the db.
        db.addParent(myParent);

        // Step 3: save parent in SP.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferencesUtils.saveParentToSP(sp, myParent);
//         Step 4: Extract from SP.
        myUser = SharedPreferencesUtils.getParentFromSP(sp);
        Log.d(TAG, String.format("myUser initialized: <%s>", myUser.toString()));
    }
}