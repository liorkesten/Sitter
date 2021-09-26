package service.sitter.login.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import service.sitter.R;
import service.sitter.databinding.FragmentSetProfileBabysitterBinding;
import service.sitter.databinding.FragmentSetProfileParentBinding;

public class SetProfileBabysitterFragment extends Fragment {
    private static final String TAG = SetProfileBabysitterFragment.class.getSimpleName();

    private MutableLiveData<Boolean> hasMobilityMutableLiveData;
    private FragmentSetProfileBabysitterBinding binding;


    public SetProfileBabysitterFragment() {
        super(R.layout.fragment_set_profile_babysitter);
        hasMobilityMutableLiveData = new MutableLiveData<>();
        ;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // inflaing
        Log.d(TAG, "instance created");
        binding = FragmentSetProfileBabysitterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // init UI Components
        Switch switchHasMoiblity = root.findViewById(R.id.mobility_switch);
        // default values
        hasMobilityMutableLiveData.setValue(switchHasMoiblity.isChecked());

        //mobility listener
        switchHasMoiblity.setOnCheckedChangeListener((buttonView, isChecked) -> hasMobilityMutableLiveData.setValue(isChecked));
        return root;
    }

    public MutableLiveData<Boolean> getLiveDataHasMobility() {
        return hasMobilityMutableLiveData;
    }

    ;

}
