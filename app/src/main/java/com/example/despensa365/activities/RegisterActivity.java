package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.despensa365.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    EditText etMailRegister, etPwdRegister, etPwdAgainRegister, etNicknameRegister;
    TextView tvTitleRegister, tvMailTitleRegister, tvPwdRegister, tvPwdAgainTitle;
    Button btnBackRegister, btnNextRegister;
    private static final String EMAIL_REGEX = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
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
        firebaseAuth = FirebaseAuth.getInstance();
        etMailRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isValidEmail(s.toString())) {
                    etMailRegister.setError(getString(R.string.invalidEmail));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etPwdRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isValidPassword(s.toString())) {
                    String pwdRules = getString(R.string.pwdRules);
                    etPwdRegister.setError(pwdRules);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etPwdAgainRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etPwdRegister.getText().toString().equals(s.toString())) {
                    String notSamePasswords = getString(R.string.notSamePasswords);
                    etPwdAgainRegister.setError(notSamePasswords);
                }
            }
        });

        btnBackRegister.setOnClickListener(v -> {
            finish();
        });

        btnNextRegister.setOnClickListener(v -> {
            String nickname = etNicknameRegister.getText().toString();
            String email = etMailRegister.getText().toString();
            String password = etPwdRegister.getText().toString();

            if (validateForm()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(it -> {
                    if (it.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nickname)
                                .build();

                        user.updateProfile(profileUpdates).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {

                                Intent newIntent = new Intent();
                                newIntent.putExtra("allowed", (etMailRegister.getText()).length() != 0);
                                setResult(RESULT_OK, newIntent);
                                finish();
                            } else {

                                Toast.makeText(this, updateTask.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(this, it.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                });
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


    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private boolean validateForm() {
        String email = etMailRegister.getText().toString();
        String password = etPwdRegister.getText().toString();
        String confirmPassword = etPwdAgainRegister.getText().toString();
        String nickname = etNicknameRegister.getText().toString();

        if (nickname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, R.string.notAllowEmpty, Toast.LENGTH_LONG).show();
            return false;
        }

        if (!isValidEmail(email)) {
            etMailRegister.setError(getString(R.string.invalidEmail));
            return false;
        }

        if (!isValidPassword(password)) {
            etPwdRegister.setError(getString(R.string.pwdRules));
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etPwdAgainRegister.setError(getString(R.string.notSamePasswords));
            return false;
        }

        return true;
    }
}
