package service.sitter.login;

import static service.sitter.login.LoginActivity.MAIL_RESULT_CODE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import service.sitter.R;

public class SignUpActivity extends AppCompatActivity {

    ImageButton backButton;

    private String firstName = "", lastName = "", mail = "", pass = "", rePass = "";
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EditText fullName = findViewById(R.id.et_name);
        EditText email = findViewById(R.id.et_email);
        EditText password = findViewById(R.id.et_password);
        EditText retypePassword = findViewById(R.id.et_repassword);
        Button registerButton = findViewById(R.id.btn_register);
        backButton = findViewById(R.id.btn_back);


        registerButton.setOnClickListener(v -> {
            boolean checkName = true, checkMail = true, checkPass = true;
            String name = fullName.getText().toString();
            String[] nameArr = name.split("\\s+");
            if (nameArr.length != 2) {
                fullName.setError("You must enter first and last name to register.");
                checkName = false;
            } else {
                firstName = nameArr[0];
                lastName = nameArr[1];
            }
            if ((firstName.isEmpty() || lastName.isEmpty()) && checkName) {
                fullName.setError("You must enter a username to register.");
                checkName = false;
            }
            mail = email.getText().toString();
            if (mail.isEmpty()) {
                email.setError("You must enter a mail address to register.");
                checkMail = false;
            }
            if (!validate(mail) && checkMail) {
                email.setError("Invalid mail address");
            }
            pass = password.getText().toString();
            if (pass.isEmpty()) {
                password.setError("You must enter a password to register.");
                checkPass = false;
            }
            rePass = retypePassword.getText().toString();
            if (rePass.isEmpty()) {
                retypePassword.setError("You must re-enter the password to register.");
                checkPass = false;
            }
            if (!rePass.equals(pass)) {
                retypePassword.setError("Passwords must match.");
                checkPass = false;
            }
            if (pass.length() < 6) {
                retypePassword.setError("Password should be at least 6 characters.");
                checkPass = false;
            }
            if (checkName && checkMail && checkPass) {
                Intent intentSetProfile = new Intent(SignUpActivity.this, SetProfileActivity.class);
                Intent loginIntent = new Intent();
                loginIntent.putExtra("firstName", firstName);
                loginIntent.putExtra("lastName", lastName);
                loginIntent.putExtra("email", mail);
                loginIntent.putExtra("password", pass);
                setResult(MAIL_RESULT_CODE, loginIntent);
                startActivity(intentSetProfile);
                finish();
            }
        });

        backButton.setOnClickListener(v -> finish());
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