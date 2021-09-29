package service.sitter.ui.parent.publishRequest;

import static java.lang.System.exit;
import static java.lang.Thread.sleep;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import service.sitter.databinding.FragmentPublishRequestBinding;
import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.models.Child;
import service.sitter.models.Parent;
import service.sitter.models.Request;
import service.sitter.recyclerview.children.ChildAdapter;
import service.sitter.ui.fragments.DateFragment;
import service.sitter.ui.fragments.LocationFragment;
import service.sitter.ui.fragments.PaymentFragment;
import service.sitter.ui.fragments.TimeFragment;
import service.sitter.utils.PrettyToastProvider;
import service.sitter.utils.SharedPreferencesUtils;

public class PublishRequestFragment extends Fragment {

    private static final String TAG = PublishRequestFragment.class.getSimpleName();
    private FragmentPublishRequestBinding binding;
    private LocalDate date = LocalDate.now();
    private String startTime = "";
    private String endTime = "";
    private int payment = 1;
    private Place location;
    List<Child> children;

    private IDataBase db;
    private SharedPreferences sp;
    private Parent myUser;
    private View layout;
    private PrettyToastProvider prettyToastProvider;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Set UI Components
        binding = FragmentPublishRequestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button publishRequestButton = root.findViewById(R.id.publish_request_button);
        EditText descriptionEditText = root.findViewById(R.id.description_edit_text);
        RecyclerView childrenRecyclerView = root.findViewById(R.id.recycler_view_children);

        LayoutInflater inflaterToast = getLayoutInflater();
        prettyToastProvider = new PrettyToastProvider(inflaterToast, root);

        // Set Logic Components
        db = DataBase.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());
        myUser = SharedPreferencesUtils.getParentFromSP(sp);
        if (myUser == null) {
            Log.e(TAG, "User doesn't exist");
            exit(101);
        }

        children = new ArrayList<>();
        // Set Logic Business Components
        DateFragment dateFragment = new DateFragment();
        TimeFragment startTimeFragment = new TimeFragment("18:00", "Start Time");
        TimeFragment endTimeFragment = new TimeFragment("20:00", "End Time");
        PaymentFragment paymentFragment = new PaymentFragment(myUser.getDefaultPricePerHour());
        LocationFragment locationFragment = new LocationFragment(myUser.getLocation());
        ChildAdapter childAdapter = new ChildAdapter(child -> {
            if (children.contains(child)) {
                children.remove(child);
            } else {
                children.add(child);
            }
        }, true, getActivity().getApplication(), true);
        childrenRecyclerView.setAdapter(childAdapter);
        childAdapter.setChildren(myUser.getChildren());
        childrenRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        // Set default values:
        setDefaultValues(dateFragment, startTimeFragment, endTimeFragment, paymentFragment, locationFragment);

        // Get Request Data
        listenToObservers(dateFragment, startTimeFragment, endTimeFragment, paymentFragment, locationFragment);


        // Adding Request
        publishRequestButton.setOnClickListener(l -> {
            if (shouldBlockPublishRequest()) {
                return;
            }
            String description = descriptionEditText.getText().toString();
            String locationID = location != null ? location.getId() : "";
            Request request = new Request(
                    myUser.getUuid(), this.date, startTime, endTime, locationID,
                    children, payment, description
            );
//            ProgressBar progressBar;
            db.addRequest(request, new IOnUploadingRequest() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "request was added successfully");
                    prettyToastProvider.showToast("Your request was added successfully.", getActivity().getApplication());
                    // TODO add once the button was clicked, move to manage requests.
//                    pDialog.cancel();

                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            });
//            progressBar = new ProgressBar(this.getContext());
//            progressBar. setMessage("loading..");
            Toast.makeText(this.getContext(), "loading", Toast.LENGTH_LONG).show();


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

    private boolean shouldBlockPublishRequest() {
        if (children.size() == 0) {
            prettyToastProvider.showToast("You must choose child before publish request", getActivity().getApplication());
            return true;
        } else if (date.isBefore(LocalDate.now())) {
            prettyToastProvider.showToast("Date can't be before the current time.", getActivity().getApplication());
            return true;
        }
        return false;
    }

    private void listenToObservers(DateFragment dateFragment, TimeFragment startTimeFragment, TimeFragment endTimeFragment, PaymentFragment paymentFragment, LocationFragment locationFragment) {
        dateFragment.getLiveData().observe(getViewLifecycleOwner(), newDate -> this.date = newDate);
        startTimeFragment.getLiveData().observe(getViewLifecycleOwner(), newStartTime -> this.startTime = newStartTime);
        endTimeFragment.getLiveData().observe(getViewLifecycleOwner(), newEndTime -> this.endTime = newEndTime);
        paymentFragment.getLiveData().observe(getViewLifecycleOwner(), payment -> this.payment = payment);
        locationFragment.getLiveData().observe(getViewLifecycleOwner(), location -> this.location = location);
    }

    private void setDefaultValues(DateFragment dateFragment, TimeFragment startTimeFragment, TimeFragment endTimeFragment, PaymentFragment paymentFragment, LocationFragment locationFragment) {
        date = dateFragment.getLiveData().getValue();
        startTime = startTimeFragment.getLiveData().getValue();
        endTime = endTimeFragment.getLiveData().getValue();
        payment = paymentFragment.getLiveData().getValue();
        location = (locationFragment.getLiveData() != null) ? locationFragment.getLiveData().getValue() : null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}