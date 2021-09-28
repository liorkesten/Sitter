package service.sitter.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.libraries.places.api.model.Place;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import service.sitter.ParentActivity;
import service.sitter.R;
import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.login.fragments.SetProfileBabysitterFragment;
import service.sitter.login.fragments.SetProfileParentFragment;
import service.sitter.models.Babysitter;
import service.sitter.models.Child;
import service.sitter.models.Parent;
import service.sitter.models.UserCategory;
import service.sitter.ui.babysitter.manageRequests.BabysitterActivity;
import service.sitter.ui.fragments.LocationFragment;
import service.sitter.utils.SharedPreferencesUtils;

public class SetProfileActivity extends AppCompatActivity {

    private final IDataBase db = DataBase.getInstance();
    SharedPreferences sp;
    private Place location;
    private String phoneNumber = "";
    UserCategory userType = UserCategory.Parent;
    ImageButton imageButtonProfilePicture;
    EditText phoneNumberEditText;
    TextView usernameTextView;
    Uri profilePictureUri;
    private int payment;
    private List<Child> children;
    private static final int RESULT_CODE_IMAGE = 100;
    private static final String TAG = SetProfileActivity.class.getSimpleName();
    private SetProfileParentFragment setProfileParentFragment;
    private SetProfileBabysitterFragment setProfileBabysitterFragment;
    private String firstName = "", lastName = "", email = "", password = "";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "created");
        sp = PreferenceManager.getDefaultSharedPreferences(getApplication());

        if (SharedPreferencesUtils.getParentFromSP(sp) != null) {
            Intent intentMainActivity = new Intent(SetProfileActivity.this, MainActivity.class);
            startActivity(intentMainActivity);
        } else if (SharedPreferencesUtils.getBabysitterFromSP(sp) != null) {
            Intent intentMainActivity = new Intent(SetProfileActivity.this, BabysitterActivity.class);
            startActivity(intentMainActivity);
        }

        setContentView(R.layout.activity_set_profile);


        // set UI components
        imageButtonProfilePicture = findViewById(R.id.profile_picture_image_button);
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        usernameTextView = findViewById(R.id.user_name_text_view);
        LocationFragment locationFragment = new LocationFragment();
        RadioGroup radioGroup = findViewById(R.id.parent_or_babysitter_radio_group);
        Button addUserButton = findViewById(R.id.add_user_button);

        // get info from registration
        Intent infoFromRegistration = getIntent();
        firstName = infoFromRegistration.getStringExtra("firstName");
        lastName = infoFromRegistration.getStringExtra("lastName");
        email = infoFromRegistration.getStringExtra("email");
        password = infoFromRegistration.getStringExtra("password");
        fillDetails();

        // set Logic Components
        children = new ArrayList<>();
        setProfileParentFragment = new SetProfileParentFragment();
        setProfileBabysitterFragment = new SetProfileBabysitterFragment();

        // radio group listener
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            userType = (checkedId == R.id.parent_radio_button) ? UserCategory.Parent : UserCategory.Babysitter;
            Log.d("SetProfile", "onCheckedChanged, isParent:  " + userType);
            Fragment fragment = (userType == UserCategory.Parent) ? setProfileParentFragment : setProfileBabysitterFragment;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.bottomFrameFragment, fragment)
                    .commit();

        });

        // profile picture button listener
        imageButtonProfilePicture.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_CODE_IMAGE); //activity result method call
        });

        // Get Profile Data
        locationFragment.getLiveData().observe(this, location -> this.location = location);
        // Clicking on adding user
        addUserButton.setOnClickListener(l -> {
            addUser();
        });

        // Rendering Fragments
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.location_fragment_container_view, locationFragment)
                .replace(R.id.bottomFrameFragment, setProfileParentFragment)
                .commit();
    }

    @SuppressLint("SetTextI18n")
    private void fillDetails() {
        if (firstName == null || lastName == null) {
            usernameTextView.setText("Welcome User");
        } else if (!firstName.isEmpty() && !lastName.isEmpty()) {
            Resources res = getResources();
            usernameTextView.setText(res.getString(R.string.welcome_message, firstName));
        }

    }

    private void addUser() {
        this.payment = setProfileParentFragment.getPayment();
        String profilePictureUriStr = (profilePictureUri != null) ? profilePictureUri.toString()
                : null;
//                : Uri.parse("android.resource://service.sitter/drawable/profile_picture_icon").toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String locationStr = location != null ? location.toString() : "";


        if (userType == UserCategory.Parent) {
            addParent(profilePictureUriStr, phoneNumber, locationStr);
        } else if (userType == UserCategory.Babysitter) {
            addBabysitter(profilePictureUriStr, phoneNumber, locationStr);
        }
    }

    private void addBabysitter(String profilePictureUriStr, String phoneNumber, String locationStr) {
        Log.d(TAG, "creating new Babysitter");
        boolean hasMobility = setProfileBabysitterFragment.getLiveDataHasMobility().getValue();
        Babysitter babysitter = new Babysitter(firstName, lastName, email, phoneNumber, locationStr, profilePictureUriStr, hasMobility);
        SharedPreferencesUtils.saveBabysitterToSP(sp, babysitter);

        db.addBabysitter(babysitter, () -> {
            Toast toast = Toast.makeText(getApplication(), String.format("Congrats %s :) you added successfully as a babysitter. you can now start look for requests.", babysitter.getFirstName()), Toast.LENGTH_LONG);
            toast.show();
            Intent intentMainActivity = new Intent(SetProfileActivity.this, BabysitterActivity.class);
            startActivity(intentMainActivity);
        });
    }

    private void addParent(String profilePictureUriStr, String phoneNumber, String locationStr) {
        Log.d(TAG, "creating new Parent");
        setProfileParentFragment.getLiveDataChildren().
                observe(this, newChildren -> this.children = new ArrayList<>(newChildren));
        Parent parent = new Parent(firstName, lastName, email, phoneNumber, locationStr, profilePictureUriStr, children, payment);
        SharedPreferencesUtils.saveParentToSP(sp, parent);

        db.addParent(parent, () -> {
            Toast toast = Toast.makeText(getApplication(), String.format("Congrats %s :) you added successfully as a parent. you can now start publish requests.", parent.getFirstName()), Toast.LENGTH_LONG);
            toast.show();
            Intent intentMainActivity = new Intent(SetProfileActivity.this, ParentActivity.class);
            startActivity(intentMainActivity);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO change it to permissions hardcoded -1
        if (resultCode == -1) {
            if (requestCode == RESULT_CODE_IMAGE) {
                profilePictureUri = data.getData();
                Picasso.get().load(profilePictureUri).into(imageButtonProfilePicture);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}