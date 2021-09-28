package service.sitter.login;//package service.sitter.login;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Patterns;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import service.sitter.MainActivity;
import service.sitter.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin, btnSignUp;
    private EditText email, password;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    static String LoggedIn_User_Email;
    private static final int RC_SIGN_IN = 9001;
    private final String TAG = LoginActivity.class.getSimpleName();
    /*    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUserRef = mRootRef.child("users");*/
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthListener;

    HashMap<String, Object> userDetails = new HashMap<>();
    List<String> list = new ArrayList<>();

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    Intent intentSetProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if user already logged in with the same phone
        firebaseAuth = FirebaseAuth.getInstance();
        intentSetProfile = new Intent(LoginActivity.this, SetProfileActivity.class);
        if (firebaseAuth.getCurrentUser() != null) {
            Log.d(TAG, String.format("User Already exists: <%s>", firebaseAuth.getCurrentUser().getDisplayName()));
            setExtras();
            startActivity(intentSetProfile);
        }
        // first time visiting or in case of in-completing signup
        else {
            setContentView(R.layout.activity_login);
            GoogleSignInButton signInButton = (GoogleSignInButton) findViewById(R.id.sign_in_button);
            btnSignUp = findViewById(R.id.buttonSignUp);
            btnLogin = findViewById(R.id.buttonLogin);
            email = findViewById(R.id.editTextEmail);
            password = findViewById(R.id.editTextPassword);
            db = FirebaseFirestore.getInstance();


            btnSignUp.setOnClickListener(this);
            btnLogin.setOnClickListener(this);

            GoogleSignInOptions options = new GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(getApplication(), options);
            signInButton.setOnClickListener(view -> signIn());
        }

    }

    private void setExtras() {
        String fullName = firebaseAuth.getCurrentUser().getDisplayName();
        intentSetProfile.putExtra("firstName", fullName.split(" ")[0]);
        intentSetProfile.putExtra("lastName", fullName.split(" ")[1]);
        intentSetProfile.putExtra("email", firebaseAuth.getCurrentUser().getEmail());
//            intentSetProfile.putExtra("password", firebaseAuth.getCurrentUser().);
    }

    private void signIn() {
        if (firebaseAuth.getCurrentUser() == null) {

            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            Log.d(TAG, "" + resultCode);
            super.onActivityResult(requestCode, resultCode, data);

            // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }


    public void updateUI(final GoogleSignInAccount googleSignInAccount) {

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
                                        setExtras();
                                        startActivity(intentSetProfile);
                                        finishAffinity();
                                        Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Log.d(TAG, "User creating - " + userID);
                                        list.clear();
                                        setExtras();
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

/*    private void writeNewUser(String userId,String email, String firstname,String lastname,String dob) {
        User user = new User(firstname,lastname, email,dob);
        mUserRef.child(userId).setValue(user);
    }*/


    @Override
    protected void onStart() {
        super.onStart();
        //firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonSignUp) {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
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
                        setExtras();
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

}