package service.sitter.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import service.sitter.R;

public class SignUpActivity extends AppCompatActivity {

    String firstName, lastName, mail, pass, rePass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EditText fullName = findViewById(R.id.et_name);
        EditText email = findViewById(R.id.et_email);
        EditText password = findViewById(R.id.et_password);
        EditText retypePassword = findViewById(R.id.et_repassword);
        Button registerButton = findViewById(R.id.btn_register);


        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = (String)s;
                String[] fullName = name.split("\\s+");
                firstName = fullName[0];
                lastName = fullName[1];
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mail = (String)s;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               pass = (String)s;


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        retypePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String re = (String) s;
                if (!re.equals(pass)){
                    //error msg
                }
                else {
                    rePass = pass;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registerButton.setOnClickListener(v -> {
            Intent intentSetProfile = new Intent(SignUpActivity.this, SetProfile.class);
            startActivity(intentSetProfile);
            finish();
        });
    }
}