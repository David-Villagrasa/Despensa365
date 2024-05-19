package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.despensa365.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {
    TextView mail, pwd;
    Button back,confirm;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth= FirebaseAuth.getInstance();

        mail = findViewById(R.id.etMailLogin);
        pwd = findViewById(R.id.etPwdLogin);
        back = findViewById(R.id.btnBackLogin);
        confirm = findViewById(R.id.btnNextLogin);
    }


    public void clickBackLogin(View v) {
        finish();
    }

    public void clickConfirmLogin(View v) {
        String email = mail.getText().toString();
        String password = pwd.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(v1 -> {
                    if (v1.isSuccessful()) {
                        Intent newIntent = new Intent();
                        setResult(RESULT_OK, newIntent);
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid email or password
                        Toast.makeText(this, getString(R.string.auth_invalid_credentials), Toast.LENGTH_LONG).show();
                    } else if (e instanceof FirebaseAuthInvalidUserException) {
                        // User not found
                        Toast.makeText(this, getString(R.string.auth_invalid_user), Toast.LENGTH_LONG).show();
                    } else {
                        // General error
                        Toast.makeText(this, getString(R.string.auth_general_error), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
