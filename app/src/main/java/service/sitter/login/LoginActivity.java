package service.sitter.login;//package service.sitter.login;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.util.ArrayList;
import java.util.List;

import service.sitter.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int GOOGLE_RESULT_CODE = 16;
    public static final int MAIL_RESULT_CODE = 17;

    private final String TAG = LoginActivity.class.getSimpleName();
    private Button signInButton, signUpButton;
    private EditText email, password;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;


    private GoogleSignInClient mGoogleSignInClient;

    private List<String> list = new ArrayList<>();

    private Intent intentSetProfile;
    private Intent intentSignUp;
    Intent googleSignInIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        intentSetProfile = new Intent(LoginActivity.this, SetProfileActivity.class);
        // check if user already logged in with the same phone

        if (firebaseAuth.getCurrentUser() != null) {
            Log.d(TAG, String.format("User Already exists: <%s>", firebaseAuth.getCurrentUser().getDisplayName()));
            startActivity(intentSetProfile);
        }
        // first time visiting or in case of in-completing signup
        else {
            setContentView(R.layout.activity_login);
            signUpButton = findViewById(R.id.buttonSignUp);
            this.signInButton = findViewById(R.id.buttonLogin);
            email = findViewById(R.id.editTextEmail);
            password = findViewById(R.id.editTextPassword);
            db = FirebaseFirestore.getInstance();

            // Set listeners on buttons:
            // Signup button
            setupSignUpButtonListener();
            this.signInButton.setOnClickListener(l -> signInAuth());
            setupSignInWithGoogleButton();
            setUpSignInWithFacebookButton();
        }

    }

    private void setUpSignInWithFacebookButton() {
        //TODO
    }

    private void setupSignInWithGoogleButton() {
        GoogleSignInButton googleSignInButton = (GoogleSignInButton) findViewById(R.id.sign_in_google_button);
        GoogleSignInOptions options = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getApplication(), options);
        googleSignInButton.setOnClickListener(view -> signInWithGoogle());

    }

    private void setupSignUpButtonListener() {
        signUpButton.setOnClickListener(l -> {
            intentSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivityForResult(intentSignUp, MAIL_RESULT_CODE);
        });
    }

    private void signInWithGoogle() {
        if (firebaseAuth.getCurrentUser() == null) {
            googleSignInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(googleSignInIntent, GOOGLE_RESULT_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "requestCode" + requestCode);
        Log.d(TAG, "resultCode" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            Log.e(TAG, "The result was failed - result code equals to " + resultCode);
            return;
        } else if (resultCode == MAIL_RESULT_CODE) {
            Log.d(TAG, "Entered to signup result code");
            intentSignUp = data;
            signupAuth();
        }
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        else if (requestCode == GOOGLE_RESULT_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.android:noHistory
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            googleHandleSignInResult(task);
        }
    }

    private void googleHandleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(getApplicationContext(), "Signing Success", Toast.LENGTH_SHORT).show();
            // Signed in successfully, show authenticated UI.
            googleUpdateUI(account);
        } catch (ApiException e) {
            Toast.makeText(getApplicationContext(), "Signing Failed", Toast.LENGTH_SHORT).show();
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            googleUpdateUI(null);
        }
    }


    public void googleUpdateUI(final GoogleSignInAccount googleSignInAccount) {

//        Log.d("token", googleSignInAccount.getIdToken());
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);


        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(LoginActivity.this, AuthResultTask -> {
                    if (AuthResultTask.isSuccessful()) {
                        //final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (googleSignInAccount != null) {
                            final String userID = user.getUid();
                            Log.d(TAG, userID.toString());
                            db.collection("Users").get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        list.add(document.getId());
                                        Log.d(TAG, "" + list);

                                    }
                                    if (list.contains(userID)) {
                                        Log.d(TAG, "User already exist - " + userID);
                                        list.clear();
                                        startActivity(intentSetProfile);
                                        finishAffinity();
                                        Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Log.d(TAG, "User creating - " + userID);
                                        list.clear();
                                        startActivity(intentSetProfile);
                                        finishAffinity();
                                    }

                                } else {
                                    Toast.makeText(LoginActivity.this, "Error while saving data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonSignUp) {
//            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        } else {
            String getemail = email.getText().toString().trim();
            String getepassword = password.getText().toString().trim();

            if (getemail.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Type Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(getemail).matches()) {
                Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (getepassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Type Password", Toast.LENGTH_SHORT).show();
                return;
            }


            callsignin(getemail, getepassword);

        }
    }

    private void callsignin(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    Log.d("TESTING", "sign In Successful:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Log.w("TESTING", "signInWithEmail:failed", task.getException());
                        Toast.makeText(LoginActivity.this, "" + (task.getException().getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.") ? task.getException().getMessage().replace("There is no user record corresponding to this identifier. The user may have been deleted.", "User not found. Please register") : task.getException().getMessage().replace("The password is invalid or the user does not have a password.", "Password entered is incorrect.")), Toast.LENGTH_LONG).show();
                    } else {
                        startActivity(intentSetProfile);
                        finish();
                    }
                });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void signupAuth() {
        Log.d(TAG, "entered to signupAuth");
        String firstName = intentSignUp.getStringExtra("firstName");
        String lastName = intentSignUp.getStringExtra("lastName");
        String email = intentSignUp.getStringExtra("email");
        String password = intentSignUp.getStringExtra("password");
        Log.d(TAG, String.format("firstName:<%s>,lastName:<%s>,email:<%s>,password:<%s>", firstName, lastName, email, password));


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        String displayName = user.getDisplayName();
                        String mail = user.getEmail();
                        Log.d(TAG, "User extracted successfully via signup. Display name:" + displayName + ".Mail:" + mail);
                        // After creating user, we should update the profile to set the display name - neeeded for SetProfile activity.
                        firebaseAuth
                                .getCurrentUser()
                                .updateProfile(new UserProfileChangeRequest
                                        .Builder()
                                        .setDisplayName(firstName + " " + lastName)
                                        .build())
                                .addOnSuccessListener(v -> {
                                    Log.d(TAG, "Updated user display name");
                                    Toast toast = Toast.makeText(this.getApplication(), "User created successfully", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    startActivity(intentSetProfile);
                                });
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        //TODO
//                            updateUI(null);
                    }
                });
    }

    public void signInAuth() {
        if (!areSignInFieldsValid()) {
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");

                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Log.d(TAG, "User extracted successfully via signup: " + user);
                        startActivity(intentSetProfile);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast t = Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }
                });
    }

    private boolean areSignInFieldsValid() {
        String getemail = email.getText().toString().trim();
        String getepassword = password.getText().toString().trim();

        if (getemail.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Type Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(getemail).matches()) {
            Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (getepassword.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Type Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}