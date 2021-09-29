package service.sitter.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;

import service.sitter.R;

public class LocationFragment extends Fragment {

    private static final String TAG = LocationFragment.class.getSimpleName();
    private MutableLiveData<Place> locationLiveData;
    private AutocompleteSupportFragment autocompleteSupportFragment;
    private String defaultId;

    public LocationFragment() {
        super(R.layout.fragment_location);
        locationLiveData = new MutableLiveData<>(/*TODO Default location?*/);
    }

    public LocationFragment(String id) {
        super(R.layout.fragment_location);
        defaultId = id;
        locationLiveData = new MutableLiveData<>(/*TODO Default location?*/);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        // Initialize the AutocompleteSupportFragment.
        String apikey = "AIzaSyAS0ehL2i_SFTq8GUhbVtshTa2oIK3p-P0";
        Places.initialize(getContext(), apikey);
        PlacesClient placesClient = Places.createClient(getContext());

        autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setHint("Enter Location");
        autocompleteSupportFragment.setCountry("ISR");
        EditText editTextLocation = (EditText) autocompleteSupportFragment.getView().findViewById(R.id.places_autocomplete_search_input);
        editTextLocation.setTextSize(20.0f);
        autocompleteSupportFragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME));

        if (defaultId != null){
            fetchDefaultLocation(placesClient, editTextLocation);
        }

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
//                placeName = place.getName();
                locationLiveData.setValue(place);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e(getTag(), "An error occured " + status);
            }
        });


        return view;
    }

    private void fetchDefaultLocation(PlacesClient placesClient, EditText editTextLocation) {
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(this.defaultId, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Log.i(TAG, "Place found: " + place.getName());
            locationLiveData.setValue(place);
            editTextLocation.setText(place.getName());

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
            }
        });
    }

    public LiveData<Place> getLiveData() {
        return locationLiveData;
    }
}
