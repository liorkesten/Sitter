package service.sitter.login.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import service.sitter.R;

public class SetProfileBabysitterFragment extends Fragment {


    public SetProfileBabysitterFragment() {
        super(R.layout.fragment_set_profile_babysitter);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_profile_babysitter, container, false);
        Log.d("fragment_set_profile_babysitter_bottom_frame onViewCreated", "created");
        return view;
    }

}
