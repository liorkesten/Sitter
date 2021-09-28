package service.sitter.login;//package service.sitter.login;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
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

import service.sitter.ParentActivity;
import service.sitter.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin, btnSignUp;
    private EditText email, password;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    static String LoggedIn_User_Email;
    private static final int RC_SIGN_IN = 9001;
    private final String TAG = "tag";
    /*    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUserRef = mRootRef.child("users");*/
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthListener;

    HashMap<String, Object> userDetails = new HashMap<>();
    List<String> list = new ArrayList<>();

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashyTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        GoogleSignInButton signInButton = (GoogleSignInButton) findViewById(R.id.sign_in_button);
        btnSignUp = findViewById(R.id.buttonSignUp);
        btnLogin = findViewById(R.id.buttonLogin);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, SetProfileActivity.class));
            finish();
        }
       /* mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){

                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finishAffinity();
                }

            }
        };*/

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

    private void signIn() {
        if (firebaseAuth.getCurrentUser() == null) {

            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task AuthResultTask) {
                        if (AuthResultTask.isSuccessful()) {
                            //final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (googleSignInAccount != null) {
                                final String userID = user.getUid();
                                Log.d("demo", userID.toString());
                                db.collection("Users").get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            list.add(document.getId());
                                            Log.d("demo", "" + list);
                                        }
                                        if (list.contains(userID)) {
                                            Log.d("demo", "User already exist - " + userID);
                                            list.clear();
                                            startActivity(new Intent(LoginActivity.this, ParentActivity.class));
                                            finishAffinity();
                                            Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_LONG).show();
                                        } else {
                                            Log.d("demo", "User creating - " + userID);
                                            /*userDetails.put("firstname", googleSignInAccount.getFirstname().trim());
                                            userDetails.put("lastname", googleSignInAccount.getLastname().trim());
                                            userDetails.put("email", googleSignInAccount.getEmail());
                                            userDetails.put("photourl",googleSignInAccount.getPhotoUrl().toString());*/
//                                                User user = new User(googleSignInAccount.getGivenName().trim(), googleSignInAccount.getFamilyName().trim(), googleSignInAccount.getEmail(), userID, googleSignInAccount.getPhotoUrl().toString());
//                                                db.collection("Users").document(userID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void aVoid) {
//                                                        Log.d("demo", "Details saved successfully");
//                                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                                        finishAffinity();
//                                                        Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_LONG).show();
//                                                    }
//                                                });
                                            list.clear();
                                        }

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Error while saving data", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }

                        } else {
                            Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TESTING", "sign In Successful:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TESTING", "signInWithEmail:failed", task.getException());

                            Toast.makeText(LoginActivity.this, "" + (task.getException().getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.") ? task.getException().getMessage().replace("There is no user record corresponding to this identifier. The user may have been deleted.", "User not found. Please register") : task.getException().getMessage().replace("The password is invalid or the user does not have a password.", "Password entered is incorrect.")), Toast.LENGTH_LONG).show();
                        } else {
                            Intent i = new Intent(LoginActivity.this, ParentActivity.class);
                            i.putExtra("userid", task.getResult().getUser().getUid());
                            startActivity(i);
                            finish();
                        }
                    }
                });

    }

}