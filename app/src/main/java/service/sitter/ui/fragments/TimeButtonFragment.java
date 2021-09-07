package service.sitter.ui.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import service.sitter.R;
import service.sitter.interfaces.IOnTimeButtonClickListener;

public class TimeButtonFragment extends Fragment {
    public IOnTimeButtonClickListener listener = null;
    private String buttonText;
    private TimeButtonViewModel timeButtonViewModel;


    public TimeButtonFragment(String buttonText, IOnTimeButtonClickListener listener) {
        super(R.layout.fragment_time_button);
        this.buttonText = buttonText;
        this.listener = listener;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        timeButtonViewModel =
                new ViewModelProvider(this).get(TimeButtonViewModel.class);

        Button buttonTime = (Button) view.findViewById(R.id.button_time_dialog);
        buttonTime.setText(buttonText);
        buttonTime.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            TimePickerDialog tp = new TimePickerDialog(getActivity(), (view1, hourOfDay, minute) -> {
                buttonText = hourOfDay + ":" + minute;
                buttonTime.setText(buttonText);
                timeButtonViewModel.setTime(hourOfDay + ":" + minute);
                if (listener != null) {
                    listener.onButtonClicked(buttonText);
                    Log.d("TimeButtonFragment", buttonText);
                }
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);

            tp.show();

        });
    }
}
