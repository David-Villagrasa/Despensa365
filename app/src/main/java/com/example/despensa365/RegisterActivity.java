package com.example.despensa365;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etMailRegister, etPwdRegister, etPwdAgainRegister, etNicknameRegister;
    TextView tvTitleRegister, tvMailTitleRegister, tvPwdRegister, tvPwdAgainTitle;
    Button btnBackRegister, btnNextRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvTitleRegister = findViewById(R.id.tvTitleRegister);
        etMailRegister = findViewById(R.id.etMailRegister);
        etPwdRegister = findViewById(R.id.etPwdRegister);
        etPwdAgainRegister = findViewById(R.id.etPwdAgainRegister);
        tvMailTitleRegister = findViewById(R.id.tvMailTitleRegister);
        tvPwdRegister = findViewById(R.id.tvPwdRegister);
        tvPwdAgainTitle = findViewById(R.id.tvPwdAgainTitle);
        etNicknameRegister = findViewById(R.id.etNicknameRegister);
        btnBackRegister = findViewById(R.id.btnBackRegister);
        btnNextRegister = findViewById(R.id.btnNextRegister);

        etPwdRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isValidPassword(s.toString())) {
                    etPwdRegister.setError("Password must be 8-16 characters with uppercase, lowercase, and symbol.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etPwdAgainRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!etPwdRegister.getText().toString().equals(s.toString())) {
                    etPwdAgainRegister.setError("Passwords do not match.");
                }
            }
        });

        btnBackRegister.setOnClickListener(v -> {
            finish();
        });

        btnNextRegister.setOnClickListener(v -> {
            if (validateForm()) {
                Intent nuevoIntent = new Intent();
                nuevoIntent.putExtra("allowed", (etMailRegister.getText()).length()!=0);

                setResult(RESULT_OK, nuevoIntent);
                finish();
            }
        });
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 16) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return false;
        }
        return true;
    }

    private boolean validateForm() {
        //TODO Logic to deal with the correct values to have an successfull account
        return true;
    }
}
