package service.sitter.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import service.sitter.R;

public class SignUpActivity extends AppCompatActivity {

    private String firstName, lastName, mail, pass, rePass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EditText fullName = findViewById(R.id.et_name);
        EditText email = findViewById(R.id.et_email);
        EditText password = findViewById(R.id.et_password);
        EditText retypePassword = findViewById(R.id.et_repassword);
        Button registerButton = findViewById(R.id.btn_register);


        registerButton.setOnClickListener(v -> {
            String name = fullName.getText().toString();
            String[] nameArr = name.split("\\s+");
            firstName = nameArr[0];
            lastName = nameArr[1];
            if (firstName.isEmpty() || lastName.isEmpty()){
                fullName.setError("You must enter a username to register.");
            }
            mail = email.getText().toString();
            if (mail.isEmpty()){
                email.setError("You must enter a mail address to register.");
            }
            pass = password.getText().toString();
            if(pass.isEmpty()){
                password.setError("You must enter a password to register.");
            }
            rePass = retypePassword.getText().toString();
            if(rePass.isEmpty()){
                retypePassword.setError("You must re-enter the password to register.");
            }
            if(!rePass.equals(pass)){
                retypePassword.setError("Passwords must match.");
            }
            else {
                Intent intentSetProfile = new Intent(SignUpActivity.this, SetProfile.class);
                intentSetProfile.putExtra("firstName", firstName);
                intentSetProfile.putExtra("lastName", lastName);
                intentSetProfile.putExtra("email", mail);
                intentSetProfile.putExtra("password", pass);
                startActivity(intentSetProfile);
                finish();
            }
        });
    }
}