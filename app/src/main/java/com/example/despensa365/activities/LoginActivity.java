package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.despensa365.R;
import com.google.firebase.auth.FirebaseAuth;

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
        firebaseAuth.signInWithEmailAndPassword(mail.getText().toString(), pwd.getText().toString()).addOnCompleteListener(v1 -> {
            if(v1.isSuccessful()){
                Intent newIntent = new Intent();
                setResult(RESULT_OK, newIntent);
                finish();
            }
        });
    }
}
