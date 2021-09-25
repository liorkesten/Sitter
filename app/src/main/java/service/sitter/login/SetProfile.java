package service.sitter.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.google.android.libraries.places.api.model.Place;

import java.io.IOException;
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
import service.sitter.models.Parent;
import service.sitter.models.Request;
import service.sitter.ui.fragments.LocationFragment;

public class SetProfile extends AppCompatActivity {

    private final IDataBase db = DataBase.getInstance();
    private Place location;
    private String phoneNumber = "";
    boolean isParent = true;
    ImageButton imageButtonProfilePicture;
    EditText phoneNumberEditText;
    TextView usernameTextView;
    Uri profilePictureUri;
    private int payment;
    private List<Child> children;
    private static final int RESULT_CODE_IMAGE = 100;
    private static final String TAG = SetProfile.class.getSimpleName();
    private SetProfileParentFragment setProfileParentFragment;
    private SetProfileBabysitterFragment setProfileBabysitterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        // set UI components
        imageButtonProfilePicture = findViewById(R.id.profile_picture_image_button);
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        usernameTextView = findViewById(R.id.user_name_text_view);
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

        // profile picture button listener
        imageButtonProfilePicture.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_CODE_IMAGE); //activity result method call
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
        String firstName = usernameTextView.getText().toString().split(" ")[1];
        String lastName = usernameTextView.getText().toString().split(" ")[2];
        if (isParent) {
            Log.d(TAG, "creating new Parent");
            setProfileParentFragment.getLiveDataChildren().
                    observe(this, newChildren -> this.children = new ArrayList<>(newChildren));
            setProfileParentFragment.getLiveDataPayment().
                    observe(this, newPayment -> this.payment = newPayment);
            Parent parent = new Parent(
                    firstName,
                    lastName,
                    "hello@gmail.com",
                    phoneNumberEditText.getText().toString(),
                    location.toString(),
                    imageButtonProfilePicture.toString(),
                    children,
                    payment);
            Log.d(TAG, parent.toString());

        } else {
            Log.d(TAG, "creating new Babysitter");
        }
        Log.d(TAG, "user was added successfully: " + wasSaved);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // hardcoded -1
        if (resultCode == -1) {
            if (requestCode == RESULT_CODE_IMAGE) {
                profilePictureUri = data.getData();
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), profilePictureUri);
                    imageButtonProfilePicture.setImageBitmap(bitmapImage);
                    db.uploadImage(profilePictureUri);
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }
}