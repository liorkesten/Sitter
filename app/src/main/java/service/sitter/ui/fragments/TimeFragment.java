package service.sitter.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import service.sitter.R;

public class TimeFragment extends Fragment {

    private String currentTime;
    private MutableLiveData<String> currentTimeLiveData;
    private final String defaultTime;
    private final String title;

    public TimeFragment(String defaultTime, String title) {
        super(R.layout.fragment_time);
        this.defaultTime = defaultTime;
        this.title = title;
        currentTimeLiveData = new MutableLiveData<>(this.defaultTime);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time, container, false);
        Log.d("TimePickerFragment onViewCreated", "created");
        ((TextView) view.findViewById(R.id.timeTitle)).setText(this.title);
        TimeButtonFragment timeButtonFragment = new TimeButtonFragment(this.defaultTime, time -> currentTimeLiveData.setValue(time));

        getChildFragmentManager()
                .beginTransaction()
                .add(view.findViewById(R.id.button_time_dialog_fragment_container_view).getId(), timeButtonFragment)
                .commit();


        return view;
    }

    public LiveData<String> getLiveData() {
        return currentTimeLiveData;
    }
}
