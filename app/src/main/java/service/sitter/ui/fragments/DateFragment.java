package service.sitter.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalDate;
import java.util.Calendar;

import service.sitter.R;

public class DateFragment extends Fragment {
    private static final String TAG = DateFragment.class.getSimpleName();

    private String currentTime;
    private MutableLiveData<LocalDate> currentDateLiveData;
    private final LocalDate defaultDate;

    public DateFragment() {
        super(R.layout.fragment_date);
        defaultDate = LocalDate.now();
        currentDateLiveData = new MutableLiveData<>(defaultDate);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Set UI Components
        View view = inflater.inflate(R.layout.fragment_date, container, false);

        Button buttonDate = (Button) view.findViewById(R.id.button_date_dialog);
        buttonDate.setText(defaultDate.toString());
        buttonDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        LocalDate newDate = LocalDate.of(year, monthOfYear, dayOfMonth);
                        buttonDate.setText(newDate.toString());
                        LocalDate oldDate = currentDateLiveData.getValue();
                        currentDateLiveData.setValue(newDate);
                        Log.d(TAG, String.format(("Date changed from <%s> to <%s>"), oldDate, newDate));

                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        });

        Log.d("DateFragment", "created4");
        return view;
    }

    public LiveData<LocalDate> getLiveData() {
        Log.d("DateFragment", currentDateLiveData.toString());
        return currentDateLiveData;
    }
}
