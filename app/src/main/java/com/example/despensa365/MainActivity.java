package com.example.despensa365;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.despensa365.activities.LoginActivity;
import com.example.despensa365.activities.PantryActivity;
import com.example.despensa365.activities.RecipeActivity;
import com.example.despensa365.activities.RegisterActivity;
import com.example.despensa365.activities.RegisterPantryActivity;
import com.example.despensa365.activities.ToBuyActivity;
import com.example.despensa365.activities.WeekActivity;
import com.example.despensa365.db.DB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public FirebaseAuth firebaseAuth;
    Button btnLogin, btnRegister, btnLogout, btnManWeek, btnManRec, btnManPantry, btnManToBuy;
    TextView tvWelcome;
    ActivityResultLauncher<Intent> customLauncher;
    ActivityResultLauncher<Intent> pantryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO: Fix bcs the init and the week and those things doesn't need to happen everytime
        DB.init();

        btnLogin = findViewById(R.id.btnLogIn);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogout = findViewById(R.id.btnLogOut);
        btnManWeek = findViewById(R.id.btnManageWeek);
        btnManRec = findViewById(R.id.btnManageRecipes);
        btnManPantry = findViewById(R.id.btnManagePantry);
        btnManToBuy = findViewById(R.id.btnManageList);
        tvWelcome = findViewById(R.id.tvWelcome);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            defaultLayout();
        } else {
            loggedLayout();
        }
        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (firebaseAuth.getCurrentUser() == null) {
                defaultLayout();
            } else {
                loggedLayout();
            }
        });
        pantryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Toast.makeText(this, "Pantry created successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PantryActivity.class);
                customLauncher.launch(intent);
            }
        });
    }

    private void defaultLayout() {
        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.INVISIBLE);
        tvWelcome.setVisibility(View.INVISIBLE);
    }

    private void loggedLayout() {
        btnLogin.setVisibility(View.INVISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);
        btnLogout.setVisibility(View.VISIBLE);
        tvWelcome.setVisibility(View.VISIBLE);
        FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
        String username = userFirebase.getDisplayName();
        tvWelcome.setText(String.format("%s %s", getString(R.string.welcome), username));
        DB.currentUser=firebaseAuth.getCurrentUser();
        DB.setupDateWeekPlan(DB.currentUser);
        DB.getAllIngredients(DB.currentUser);
    }

    public void clickLogin(View v) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        customLauncher.launch(intent);
    }

    public void clickRegister(View v) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        customLauncher.launch(intent);
    }

    public void clickLogout(View v) {
        firebaseAuth.signOut();
        defaultLayout();
    }

    public void clickWeek(View v) {
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, WeekActivity.class);
            customLauncher.launch(intent);
        } else {
            Toast.makeText(this, R.string.hintMainActivity, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickRecipes(View v) {
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
            customLauncher.launch(intent);
        } else {
            Toast.makeText(this, R.string.hintMainActivity, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickPantry(View v) {
        if (firebaseAuth.getCurrentUser() != null) {
            DB.checkPantryExists(firebaseAuth.getCurrentUser(), exists -> {
                if (exists) {
                    Intent intent = new Intent(MainActivity.this, PantryActivity.class);
                    customLauncher.launch(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, RegisterPantryActivity.class);
                    pantryLauncher.launch(intent);
                }
            });
        } else {
            Toast.makeText(this, R.string.hintMainActivity, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickToBuy(View v) {
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, ToBuyActivity.class);
            customLauncher.launch(intent);
        } else {
            Toast.makeText(this, R.string.hintMainActivity, Toast.LENGTH_SHORT).show();
        }
    }
}
