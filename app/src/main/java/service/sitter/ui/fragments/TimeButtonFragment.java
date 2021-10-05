package service.sitter.ui.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import service.sitter.R;
import service.sitter.interfaces.IOnTimeButtonClickListener;

public class TimeButtonFragment extends Fragment {
    public IOnTimeButtonClickListener listener = null;
    private String buttonText;


    public TimeButtonFragment(String buttonText, IOnTimeButtonClickListener listener) {
        super(R.layout.fragment_time_button);
        this.buttonText = buttonText;
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Button buttonTime = (Button) view.findViewById(R.id.button_time_dialog);
        buttonTime.setText(buttonText);
        buttonTime.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            TimePickerDialog tpd = new TimePickerDialog(getActivity(), TimePickerDialog.THEME_HOLO_LIGHT, (view12, hourOfDay, minute) -> {
                String hourOfDayAsStr = (hourOfDay < 10) ? "0" + hourOfDay : String.valueOf(hourOfDay);
                String minuteAsStr = (minute < 10) ? "0" + minute : String.valueOf(minute);

                buttonText = hourOfDayAsStr + ":" + minuteAsStr;
                buttonTime.setText(buttonText);
                if (listener != null) {
                    listener.onButtonClicked(buttonText);
                    //Log.d("TimeButtonFragment", buttonText);
                }
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
            tpd.show();

        });
    }
}
