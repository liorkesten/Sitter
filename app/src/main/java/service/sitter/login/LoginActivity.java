package service.sitter.login;//package service.sitter.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;

import java.util.Objects;

import service.sitter.MainActivity;
import service.sitter.R;
import service.sitter.db.DataBase;
import studios.codelight.smartloginlibrary.LoginType;
import studios.codelight.smartloginlibrary.SmartLogin;
import studios.codelight.smartloginlibrary.SmartLoginCallbacks;
import studios.codelight.smartloginlibrary.SmartLoginConfig;
import studios.codelight.smartloginlibrary.SmartLoginFactory;
import studios.codelight.smartloginlibrary.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.SmartLoginException;

public class LoginActivity extends AppCompatActivity implements SmartLoginCallbacks {

    private static final String TAG = LoginActivity.class.getSimpleName();

    public interface SmartLoginCallbacks {
        void onLoginSuccess(SmartUser user);

        void onLoginFailure(SmartLoginException e);

        SmartUser doCustomLogin();

        SmartUser doCustomSignup();
    }

    private Button customSigninButton, customSignupButton, logoutButton;
    private SignInButton googleLoginButton;
    private EditText emailEditText, passwordEditText;
    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    SmartUser currentUser;
    //GoogleApiClient mGoogleApiClient;
    SmartLoginConfig config;
    SmartLogin smartLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeActionBar();
        setContentView(R.layout.activity_login);
        bindViews();
        setListeners();

        config = new SmartLoginConfig(this, this);
        config.setFacebookPermissions(null);
        config.setGoogleApiClient(null);
        facebookLoginButton = findViewById(R.id.facebook_login_button);

    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = UserSessionManager.getCurrentUser(this);
        refreshLayout();
    }

    private void refreshLayout() {
        currentUser = UserSessionManager.getCurrentUser(this);
        if (currentUser != null) {
            Log.d("Smart Login", "Logged in user: " + currentUser.toString());
            facebookLoginButton.setVisibility(View.GONE);
            googleLoginButton.setVisibility(View.GONE);
            customSigninButton.setVisibility(View.GONE);
            customSignupButton.setVisibility(View.GONE);
            emailEditText.setVisibility(View.GONE);
            passwordEditText.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);
        } else {
            facebookLoginButton.setVisibility(View.VISIBLE);
            googleLoginButton.setVisibility(View.VISIBLE);
            customSigninButton.setVisibility(View.VISIBLE);
            customSignupButton.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.VISIBLE);
            passwordEditText.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (smartLogin != null) {
            smartLogin.onActivityResult(requestCode, resultCode, data, config);
        }
    }

    private void setListeners() {
        facebookLoginButton.setOnClickListener(v -> {
            // Perform Facebook login
            smartLogin = SmartLoginFactory.build(LoginType.Facebook);
            smartLogin.login(config);
        });

        googleLoginButton.setOnClickListener(v -> {
            // Perform Google login
            smartLogin = SmartLoginFactory.build(LoginType.Google);
            smartLogin.login(config);
        });

        customSigninButton.setOnClickListener(v -> {
            // Perform custom sign in
            Log.d(TAG, config.toString());
            smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
            smartLogin.login(config);
        });

        customSignupButton.setOnClickListener(v -> {
            // Perform custom sign up
            //smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
            //smartLogin.signup(config);
            Intent intentRegister = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intentRegister);
        });

        logoutButton.setOnClickListener(v -> {
            if (currentUser != null) {
                if (currentUser instanceof SmartFacebookUser) {
                    smartLogin = SmartLoginFactory.build(LoginType.Facebook);
                } else if (currentUser instanceof SmartGoogleUser) {
                    smartLogin = SmartLoginFactory.build(LoginType.Google);
                } else {
                    smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                }
                boolean result = smartLogin.logout(LoginActivity.this);
                if (result) {
                    refreshLayout();
                    Toast.makeText(LoginActivity.this, "User logged out successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bindViews() {
        facebookLoginButton = findViewById(R.id.facebook_login_button);
        googleLoginButton = findViewById(R.id.google_login_button);
        customSigninButton = findViewById(R.id.custom_signin_button);
        customSignupButton = findViewById(R.id.custom_signup_button);
        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        logoutButton = findViewById(R.id.logout_button);
    }

    private FacebookCallback<LoginResult> mCallBack= new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            Intent signInToMain = new Intent("login.SetProfile");
            startActivity(signInToMain);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

        @Override
    public void onLoginSuccess(SmartUser user) {
        Log.d("Lior", "success");
        Toast.makeText(this, user.toString(), Toast.LENGTH_SHORT).show();
        refreshLayout();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(SmartLoginException e) {
        Log.d("Noam", "Failed to login");
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public SmartUser doCustomLogin() {
        SmartUser user = new SmartUser();
        user.setEmail(emailEditText.getText().toString());
        Log.d("Dana", "custom Login");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

        return user;
    }

    @Override
    public SmartUser doCustomSignup() {
        SmartUser user = new SmartUser();
        Intent intentRegister = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intentRegister);
        //user.setEmail(emailEditText.getText().toString());
        return user;
    }

    public void removeActionBar() {
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
    }


    private void updateUI(boolean signedIn) {
        if (signedIn) {
            googleLoginButton.setVisibility(View.GONE);
            Intent signInToMain = new Intent("login.SetProfile");
            startActivity(signInToMain);
        } else {
            googleLoginButton.setVisibility(View.VISIBLE);

        }
    }
}


//*************************************************************************************//

//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.Signature;
//import android.os.Bundle;
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.OptionalPendingResult;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.common.api.Status;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//import service.sitter.MainActivity;
//import service.sitter.R;
//
//public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
//
//    private LoginButton btnFbLogin;
//    //private TextView status;
//    public CallbackManager callbackManager;
//
//    private static final String TAG = LoginActivity.class.getSimpleName();
//    private static final int RC_SIGN_IN = 007;
//
//    private GoogleApiClient mGoogleApiClient;
//    private ProgressDialog mProgressDialog;
//
//    private SignInButton btnGooSignIn;
//    private Button btnSignOut, btnRevokeAccess;
//    private LinearLayout llProfileLayout;
//    //private ImageView imgProfilePic;
//    //private TextView txtStatus, txtName, txtEmail;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        setContentView(R.layout.activity_login);
//
//        //allocation variables to their respective buttons/images/etc
//        btnFbLogin = (LoginButton) findViewById(R.id.facebook_login_button);
//        btnGooSignIn = (SignInButton) findViewById(R.id.google_login_button);
//        btnSignOut = (Button) findViewById(R.id.logout_button);
////        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
////        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
////        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
////        txtName = (TextView) findViewById(R.id.txtName);
////        txtEmail = (TextView) findViewById(R.id.txtEmail);
//
//        // when button clicked, stay on this activity
//        btnGooSignIn.setOnClickListener(this);
//        btnSignOut.setOnClickListener(this);
//        //btnRevokeAccess.setOnClickListener(this);
//        initiallizeControls();
//
//        //Declaring GoogleSignInOptions to sign in via email
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        //Connecting the GoogleSignInOptions to the API client
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//
//        // Customizing Google Sign In button
//        btnGooSignIn.setSize(SignInButton.SIZE_STANDARD);
//        btnGooSignIn.setScopes(gso.getScopeArray());
//
////        try {
////            //Creating a hash code for the Google Sign In with exceptions for different scenarios.
////            PackageInfo info = getPackageManager().getPackageInfo("<CENSORED>", PackageManager.GET_SIGNATURES);
////            for (Signature signature : info.signatures) {
////                MessageDigest md;
////                md = MessageDigest.getInstance("SHA");
////                md.update(signature.toByteArray());
////                String something = new String(Base64.encode(md.digest(), 0));
////                //String something = new String(Base64.encodeBytes(md.digest()));
////                Log.e("hash key", something);
////            }
////        } catch (PackageManager.NameNotFoundException e1) {
////            Log.e("name not found", e1.toString());
////        } catch (NoSuchAlgorithmException e) {
////            Log.e("no such an algorithm", e.toString());
////        } catch (Exception e) {
////            Log.e("exception", e.toString());
////        }
//
//    }
//
//    //Declaring the callbackManager, what the txtStatus and Facebook login buttons are
//    private void initiallizeControls() {
//        callbackManager = CallbackManager.Factory.create();
//        //txtStatus = (TextView) findViewById(R.id.txt_status);
//        btnFbLogin = (LoginButton) findViewById(R.id.facebook_login_button);
//    }
//
//    // Specifying logging into Facebook (Google sign in bypassed), ans what happens if successful, user cancels the action, or if it fails.
//    private void loginWithFb() {
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                //txtStatus.setText("Login Success\n" + loginResult.getAccessToken());
//                Intent a = new Intent(LoginActivity.this, SetProfile.class);
//                startActivity(a);
//
//            }
//
//            @Override
//            public void onCancel() {
//                //txtStatus.setText("Login Cancelled");
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                //txtStatus.setText("Login Error: " + error.getMessage());
//
//            }
//        });
//    }
//
//    //For Google Sign in, when the Google sign in is completed, this handles its result by sending the result to the handleSignInResult();
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }
//
//    //Signing in with Google and sending the user to a new intent/page when finished
//    private void signInWithGoogle() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//        Intent gmail = new Intent(LoginActivity.this, SetProfile.class);
//
//    }
//
//    // To sign out the user from Google
//    private void signOut() {
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        updateUI(false);
//                    }
//                });
//    }
//
//    // To allow the user to revoke access to Google Sign in.
//    private void revokeAccess() {
//        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        updateUI(false);
//                    }
//                });
//    }
//
//    //This sets the user details from Google and inserts them into text or image views. This was just used for testing originally.
//    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
//        if (result.isSuccess()) {
//            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
//
//            Log.e(TAG, "display name: " + acct.getDisplayName());
//
//            String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
//            String email = acct.getEmail();
//
//            Log.e(TAG, "Name: " + personName + ", email: " + email
//                    + ", Image: " + personPhotoUrl);
//
//            //txtName.setText(personName);
//            //txtEmail.setText(email);
//
//            //External directory that assists in inserting images into ImageViews.
//            //Glide.with(getApplicationContext()).load(personPhotoUrl)
//                    //.into(imgProfilePic);
//
//            updateUI(true);
//        } else {
//            // Signed out, show unauthenticated UI.
//            updateUI(false);
//        }
//    }
//
//    //This resolves the question, "what happens when user clicks Button X, Y or Z?"
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//
//        switch (id) {
//            case R.id.google_login_button:
//                signInWithGoogle();
//                break;
//
//            case R.id.facebook_login_button:
//                loginWithFb();
//                break;
//
//            case R.id.logout_button:
//                signOut();
//                break;
//            case R.id.custom_signup_button:
//                Intent intentRegister = new Intent(LoginActivity.this, SignUpActivity.class);
//                startActivity(intentRegister);
//                break;
//
////            case R.id.btn_revoke_access:
////                revokeAccess();
////                break;
//        }
//    }
//
//    //This is code directly from the Google Tutorial that wasn't coded correctly for this activity.
//    /*@Override
//public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    super.onActivityResult(requestCode, resultCode, data);
//
//    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//    if (requestCode == RC_SIGN_IN) {
//        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//        handleSignInResult(result);
//    }
//}*/
//
//    //Google's Sign in that deals with the result of the Google Sign in and updates the buttons to be hidden/visible.
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
//    }
//
//    // If the connection fails to sign in with Google, it is logged.
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
//        // be available.
//        Log.d(TAG, "onConnectionFailed: " + connectionResult);
//    }
//
//    //Shows thw progress of the Google sign in.
//    private void showProgressDialog() {
//        if (mProgressDialog == null){
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setMessage("Loading...");
//            mProgressDialog.setIndeterminate(true);
//        }
//
//        mProgressDialog.show();
//    }
//
//    //When the Google Sign N progress is finished (regardless of result), the progress bar is hidden.
//    private void hideProgressDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.hide();
//        }
//    }
//
//    // This changes the visibility of certain buttons, depending on what buttons are needed
//    private void updateUI(boolean isSignedIn) {
//        if (isSignedIn) {
//            btnGooSignIn.setVisibility(View.GONE);
//            btnSignOut.setVisibility(View.VISIBLE);
//            btnRevokeAccess.setVisibility(View.VISIBLE);
//            //llProfileLayout.setVisibility(View.VISIBLE);
//        }
//        else {
//            btnGooSignIn.setVisibility(View.VISIBLE);
//            btnSignOut.setVisibility(View.GONE);
//            btnRevokeAccess.setVisibility(View.GONE);
//            //llProfileLayout.setVisibility(View.GONE);
//        }
//    }
//}