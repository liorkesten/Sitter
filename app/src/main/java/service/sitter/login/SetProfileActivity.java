package service.sitter.login;

import static java.lang.System.exit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import service.sitter.ParentActivity;
import service.sitter.R;
import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.db.UserNotFoundException;
import service.sitter.login.fragments.SetProfileBabysitterFragment;
import service.sitter.login.fragments.SetProfileParentFragment;
import service.sitter.models.Babysitter;
import service.sitter.models.Child;
import service.sitter.models.Parent;
import service.sitter.models.UserCategory;
import service.sitter.ui.babysitter.manageRequests.BabysitterActivity;
import service.sitter.ui.fragments.LocationFragment;
import service.sitter.ui.parent.connections.IOnGettingBabysitterFromDb;
import service.sitter.utils.SharedPreferencesUtils;

public class SetProfileActivity extends AppCompatActivity {
    private static final String TAG = SetProfileActivity.class.getSimpleName();

    private IDataBase db;
    SharedPreferences sp;
    private Place location;
    UserCategory userType = UserCategory.Parent;
    ImageButton imageButtonProfilePicture;
    EditText phoneNumberEditText;
    TextView usernameTextView;
    Uri profilePictureUri;
    private int payment;
    private List<Child> children;
    private static final int RESULT_CODE_IMAGE = 100;
    private SetProfileParentFragment setProfileParentFragment;
    private SetProfileBabysitterFragment setProfileBabysitterFragment;
    private String firstName = "", lastName = "", email = "";
    public static final Pattern VALID_PHONE_NUMBER =
            Pattern.compile("05\\d{8}");

    FirebaseUser currentUser;
    ImageButton backButtonSetProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG, "created");
        // get info from registration
        db = DataBase.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(getApplication());
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            //Log.d(TAG, "Error with fetching user - user is null");
            exit(101);
        }

        // Choose flow:
        if (canSkipSetProfileActivityUsingSP()) {
            skipSetProfileActivityUsingSP();
        }
        //Log.d(TAG, String.format("Trying to find babysitter with ID <%s> ", currentUser.getUid()));
        db.getBabysitter(currentUser.getUid(), new IOnGettingBabysitterFromDb() {
            @Override
            public void babysitterFound(Babysitter babysitter) {
                if (babysitter == null) {
                    db.getParent(currentUser.getUid(),
                            p -> {
                                if (p == null) {
                                    stayInSetProfileActivity();
                                } else {
                                    SharedPreferencesUtils.saveParentToSP(sp, p);
                                    moveToParentActivity();
                                }
                            }, null);
                } else {
                    //Log.d(TAG, "Babysitter found: " + babysitter);
                    SharedPreferencesUtils.saveBabysitterToSP(sp, babysitter);
                    moveToBabysitterActivity();
                }
            }

            @Override
            public void onFailure(String phoneNumber) {
                //Log.d(TAG, String.format("Babysitter with ID <%s> didn't found: ", currentUser.getUid()));
                //Log.d(TAG, String.format("Trying to find parent with ID <%s> ", currentUser.getUid()));

            }
        });
//        else if (canSkipSetProfileActivityUsingDB()) {
//            skipSetProfileActivityUsingDB();
//        } else {
//            stayInSetProfileActivity();
//        }


    }

    private void stayInSetProfileActivity() {
        setContentView(R.layout.activity_set_profile);

        // set UI components
        imageButtonProfilePicture = findViewById(R.id.profile_picture_image_button);
        backButtonSetProfile = findViewById(R.id.backButtonSetProfile);
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        usernameTextView = findViewById(R.id.user_name_text_view);
        LocationFragment locationFragment = new LocationFragment();
        RadioGroup radioGroup = findViewById(R.id.parent_or_babysitter_radio_group);
        Button addUserButton = findViewById(R.id.add_user_button);


        setValuesFromUserAccount();
        fillDetails();

        // set Logic Components
        children = new ArrayList<>();
        setProfileParentFragment = new SetProfileParentFragment();
        setProfileBabysitterFragment = new SetProfileBabysitterFragment();

        // radio group listener
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            userType = (checkedId == R.id.parent_radio_button) ? UserCategory.Parent : UserCategory.Babysitter;
            //Log.d("SetProfile", "onCheckedChanged, isParent:  " + userType);
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
        addUserButton.setOnClickListener(l -> addUser());

        // Rendering Fragments
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.location_fragment_container_view, locationFragment)
                .replace(R.id.bottomFrameFragment, setProfileParentFragment)
                .commit();

        backButtonSetProfile.setOnClickListener(v -> finish());
    }

    private void skipSetProfileActivityUsingDB() {
        try {
            Babysitter b = db.getBabysitter(currentUser.getUid());
            SharedPreferencesUtils.saveBabysitterToSP(sp, b);
            moveToBabysitterActivity();

        } catch (UserNotFoundException e) {
            try {
                Parent p = db.getParent(currentUser.getUid());
                SharedPreferencesUtils.saveParentToSP(sp, p);
                moveToParentActivity();
            } catch (UserNotFoundException ignored) {
            }
        }
    }

    private boolean canSkipSetProfileActivityUsingDB() {
        try {
            db.getBabysitter(currentUser.getUid());
            return true;
        } catch (UserNotFoundException e) {
            try {
                db.getParent(currentUser.getUid());
                return true;
            } catch (UserNotFoundException userNotFoundException) {
                return false;
            }
        }
    }

    private void skipSetProfileActivityUsingSP() {
        if (SharedPreferencesUtils.getParentFromSP(sp) != null) {
            Intent intentMainActivity = new Intent(SetProfileActivity.this, ParentActivity.class);
            startActivity(intentMainActivity);
        } else if (SharedPreferencesUtils.getBabysitterFromSP(sp) != null) {
            Intent intentMainActivity = new Intent(SetProfileActivity.this, BabysitterActivity.class);
            startActivity(intentMainActivity);
        }
    }

    private boolean canSkipSetProfileActivityUsingSP() {
        return (SharedPreferencesUtils.getParentFromSP(sp) != null) ||
                SharedPreferencesUtils.getBabysitterFromSP(sp) != null;
    }

    private void setValuesFromUserAccount() {
        firstName = currentUser.getDisplayName().split(" ")[0];
        lastName = currentUser.getDisplayName().split(" ")[1]; // TODO Maybe it won't work with google auth.
        email = currentUser.getEmail();
    }

    @SuppressLint("SetTextI18n")
    private void fillDetails() {
        if (currentUser.getDisplayName() == null || currentUser.getDisplayName().equals("")) {
            usernameTextView.setText("Welcome User");
        } else {
            Resources res = getResources();
            usernameTextView.setText(res.getString(R.string.welcome_message, firstName));
        }
    }

    private void addUser() {
        boolean allGood = true;
        this.payment = setProfileParentFragment.getPayment();
        String profilePictureUriStr = (profilePictureUri != null) ? profilePictureUri.toString()
                : null;
//                : Uri.parse("android.resource://service.sitter/drawable/profile_picture_icon").toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String locationStr = location != null ? location.getId() : "";
        Matcher matcher = VALID_PHONE_NUMBER.matcher(phoneNumber);
        if (!matcher.matches()) {
            phoneNumberEditText.setError("Invalid phone number");
            allGood = false;
        }
        if (allGood) {
            if (userType == UserCategory.Parent) {
                addParent(profilePictureUriStr, phoneNumber, locationStr);
            } else if (userType == UserCategory.Babysitter) {
                addBabysitter(profilePictureUriStr, phoneNumber, locationStr);
            }
        }
    }

    private void addBabysitter(String profilePictureUriStr, String phoneNumber, String locationStr) {
        //Log.d(TAG, "creating new Babysitter");
        boolean hasMobility = setProfileBabysitterFragment.getLiveDataHasMobility().getValue();
        Babysitter babysitter = new Babysitter(firstName, lastName, email, phoneNumber, locationStr, profilePictureUriStr, hasMobility);
        babysitter.setUuid(currentUser.getUid());

        SharedPreferencesUtils.saveBabysitterToSP(sp, babysitter);

        db.addBabysitter(babysitter, () -> {
            Toast toast = Toast.makeText(getApplication(), String.format("Congrats %s :) you added successfully as a babysitter. you can now start look for requests.", babysitter.getFirstName()), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            moveToBabysitterActivity();
        });
    }

    private void addParent(String profilePictureUriStr, String phoneNumber, String locationStr) {
        //Log.d(TAG, "creating new Parent");
        setProfileParentFragment.getLiveDataChildren().
                observe(this, newChildren -> this.children = new ArrayList<>(newChildren));
        Parent parent = new Parent(firstName, lastName, email, phoneNumber, locationStr, profilePictureUriStr, children, payment);
        parent.setUuid(currentUser.getUid());
        // validating that added children
        if (children.isEmpty()) {
            Toast.makeText(this, "Parent must insert children to continue", Toast.LENGTH_LONG).show();
            return;
        }

        db.addParent(parent, () -> {
            Toast toast = Toast.makeText(getApplication(), String.format("Congrats %s ! you were added successfully as a parent. Now you can start publishing requests.", parent.getFirstName()), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            SharedPreferencesUtils.saveParentToSP(sp, parent);
            moveToParentActivity();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO change it to permissions hardcoded -1
        if (resultCode == -1) {
            if (requestCode == RESULT_CODE_IMAGE) {
                profilePictureUri = Uri.parse(data.getData().toString());
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

    private void moveToBabysitterActivity() {
        startActivity(new Intent(SetProfileActivity.this, BabysitterActivity.class));
    }

    private void moveToParentActivity() {
        startActivity(new Intent(SetProfileActivity.this, ParentActivity.class));
    }
}