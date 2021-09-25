package service.sitter.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.google.android.libraries.places.api.model.Place;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import service.sitter.R;
import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.db.RequestsDataBase;
import service.sitter.login.fragments.SetProfileBabysitterFragment;
import service.sitter.login.fragments.SetProfileParentFragment;
import service.sitter.models.Child;
import service.sitter.models.Request;
import service.sitter.ui.fragments.LocationFragment;

public class SetProfile extends AppCompatActivity {

    private IDataBase db = DataBase.getInstance();
    private Place location;
    private String phoneNumber = "";
    boolean isParent = true;
    private List<Child> children;
    private static final String TAG = SetProfile.class.getSimpleName();
    private SetProfileParentFragment setProfileParentFragment;
    private SetProfileBabysitterFragment setProfileBabysitterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        // set UI components
        LocationFragment locationFragment = new LocationFragment();
        SetProfileParentFragment setProfileBottomFrameFragment = new SetProfileParentFragment();
        RadioGroup radioGroup = findViewById(R.id.parent_or_babysitter_radio_group);
        Button addUserButton = findViewById(R.id.add_user_button);

        // set Logic Components
        children = new ArrayList<>();
        setProfileParentFragment = new SetProfileParentFragment();
        setProfileBabysitterFragment = new SetProfileBabysitterFragment();

        // radio group listener
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            isParent = checkedId == R.id.parent_radio_button;
            Log.d("SetProfile", "onCheckedChanged, isParent:  " + isParent);
            Fragment fragment = isParent ? setProfileParentFragment : setProfileBabysitterFragment;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.bottomFrameFragment, fragment)
                    .commit();

        });

        // Get Profile Data
        locationFragment.getLiveData().observe(this, location -> this.location = location);


        // Adding Request
        addUserButton.setOnClickListener(l -> {
            Log.d(TAG, "trying too add");
            addUser();
        });

        // Rendering Fragments
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.location_fragment_container_view, locationFragment)
                .add(R.id.bottomFrameFragment, setProfileBottomFrameFragment)
                .commit();
    }

    private void addUser() {
        boolean wasSaved = false;
        if (isParent) {
            Log.d(TAG, "creating new Parent");
            setProfileParentFragment.getLiveData().
                    observe(this, newChildren -> this.children = new ArrayList<>(newChildren));
            Log.d(TAG, children.toString());
        } else {
            Log.d(TAG, "creating new Babysitter");
        }
        Log.d(TAG, "user was added successfully: " + wasSaved);
    }
}