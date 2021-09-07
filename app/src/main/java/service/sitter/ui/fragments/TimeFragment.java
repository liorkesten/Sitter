package service.sitter.ui.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import java.util.Calendar;

import service.sitter.R;

public class TimeFragment extends Fragment {

    private String currentTime;

//    public TimeFragment() {
//        super(R.layout.fragment_time);
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time, container, false);
        Log.d("TimePickerFragment onViewCreated", "created");
        currentTime = "Lior";

        TimeButtonFragment timeButtonFragment = new TimeButtonFragment("00:00", null);

        getChildFragmentManager()
                .beginTransaction()
                .add(view.findViewById(R.id.button_time_dialog_fragment_container_view).getId(), timeButtonFragment)
                .commit();


        return view;
    }
}
