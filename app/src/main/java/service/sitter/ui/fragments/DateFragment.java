package service.sitter.ui.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;

import service.sitter.R;

public class DateFragment extends Fragment {

    private String currentTime;
    private MutableLiveData<String> currentDateLiveData;
    private final String defaultDate;

    public DateFragment(String defaultDate) {
        super(R.layout.fragment_date);
        this.defaultDate = defaultDate;
        currentDateLiveData = new MutableLiveData<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Set UI Components
        View view = inflater.inflate(R.layout.fragment_date, container, false);

        Button buttonDate = (Button) view.findViewById(R.id.button_date_dialog);
        buttonDate.setText(defaultDate);
        buttonDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        String chosenDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        buttonDate.setText(chosenDate);
                        currentDateLiveData.setValue(chosenDate);
                        Log.d("DateFragment", "after setting" + currentDateLiveData.toString());

                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        Log.d("DateFragment", "created4");
        return view;
    }

    public LiveData<String> getLiveData() {
        Log.d("DateFragment", currentDateLiveData.toString());
        return currentDateLiveData;
    }
}
