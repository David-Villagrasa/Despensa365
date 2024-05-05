package com.example.despensa365;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    TextView mail, pwd;
    Button back,confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mail = findViewById(R.id.etMailLogin);
        pwd = findViewById(R.id.etPwdLogin);
        back = findViewById(R.id.btnBackLogin);
        confirm = findViewById(R.id.btnNextLogin);
    }


    public void clickBackLogin(View v) {
        finish();
    }

    public void clickConfirmLogin(View v) {
        Intent nuevoIntent = new Intent();
        nuevoIntent.putExtra("allowed", (mail.getText()).length()!=0);

        setResult(RESULT_OK, nuevoIntent);
        finish();
    }
}
