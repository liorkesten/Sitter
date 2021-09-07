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
import service.sitter.interfaces.IOnButtonClickListener;
import service.sitter.ui.home.HomeViewModel;

public class TimeButtonFragment extends Fragment {
    public IOnButtonClickListener listener = null;
    private String buttonTitle;
    private TimeButtonViewModel timeButtonViewModel;


    public TimeButtonFragment(String buttonTitle, IOnButtonClickListener listener)
    {
        super(R.layout.fragment_time_button);
        this.buttonTitle = buttonTitle;
        this.listener = listener;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        timeButtonViewModel =
                new ViewModelProvider(this).get(TimeButtonViewModel.class);

        Button buttonTime = (Button) view.findViewById(R.id.button_time_dialog);
        buttonTime.setText(buttonTitle);
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                TimePickerDialog tp = new TimePickerDialog(getActivity(), (view1, hourOfDay, minute) -> {
                    buttonTime.setText(hourOfDay + ":" + minute);
                    timeButtonViewModel.setTime(hourOfDay + ":" + minute);


                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);

                tp.show();
                if (listener != null){
                    buttonTitle = listener.onButtonClicked();
                    Log.d("TimeButtonFragment", buttonTitle);
                    buttonTime.setText(buttonTitle);
                }
            }
        });
    }
}
