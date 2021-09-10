package service.sitter.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;


import java.util.Arrays;

import service.sitter.R;

public class LocationFragment extends Fragment {
    private MutableLiveData<String> locationLiveData;
    private String location;
    private AutocompleteSupportFragment autocompleteSupportFragment;

    public LocationFragment() {
        super(R.layout.fragment_location);

        locationLiveData = new MutableLiveData<>();
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
        ((EditText) autocompleteSupportFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setTextSize(20.0f);
        autocompleteSupportFragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                location = place.getName();
                locationLiveData.setValue(location);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e(getTag(), "An error occured " + status);
            }
        });


        return view;
    }

    public LiveData<String> getLiveData() {
        return locationLiveData;
    }
}
