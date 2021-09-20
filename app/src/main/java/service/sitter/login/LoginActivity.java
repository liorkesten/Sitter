package service.sitter.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;

import java.util.Objects;

import service.sitter.MainActivity;
import service.sitter.R;
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
            smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
            smartLogin.login(config);
        });

        customSignupButton.setOnClickListener(v -> {
            // Perform custom sign up
            smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
            smartLogin.signup(config);
        });

        logoutButton.setOnClickListener(v -> {
            if (currentUser != null) {
                if (currentUser instanceof SmartFacebookUser) {
                    smartLogin = SmartLoginFactory.build(LoginType.Facebook);
                } else if(currentUser instanceof SmartGoogleUser) {
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

    @Override
    public void onLoginSuccess(SmartUser user) {
        Log.d("Lior","success");
        Toast.makeText(this, user.toString(), Toast.LENGTH_SHORT).show();
        refreshLayout();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(SmartLoginException e) {
        Log.d("Noam","success");
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public SmartUser doCustomLogin() {
        SmartUser user = new SmartUser();
        user.setEmail(emailEditText.getText().toString());

        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

        return user;
    }

    @Override
    public SmartUser doCustomSignup() {
        SmartUser user = new SmartUser();
        user.setEmail(emailEditText.getText().toString());
        return user;
    }

    public void removeActionBar() {
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored){}
    }
}