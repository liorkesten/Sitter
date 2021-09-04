package service.sitter.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import service.sitter.R;
import service.sitter.interfaces.IOnButtonClickListener;

public class TimeDialogButtonFragment extends Fragment {
    public IOnButtonClickListener listener = null;
    private String buttonTitle;

    public TimeDialogButtonFragment(String buttonTitle, IOnButtonClickListener listener)
    {
        super(R.layout.fragment_time_dialog_button);
        this.buttonTitle = buttonTitle;
        this.listener = listener;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button buttonTimeDialog = (Button) view.findViewById(R.id.button_time_dialog);
        buttonTimeDialog.setText(buttonTitle);
        buttonTimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    buttonTitle = listener.onButtonClicked();
                    buttonTimeDialog.setText(buttonTitle);
                }
            }
        });
    }
}
