package com.example.despensa365;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.despensa365.activities.LoginActivity;
import com.example.despensa365.activities.PantryActivity;
import com.example.despensa365.activities.RecipeActivity;
import com.example.despensa365.activities.RegisterActivity;
import com.example.despensa365.activities.ToBuyActivity;
import com.example.despensa365.activities.WeekActivity;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnRegister, btnLogout, btnManWeek, btnManRec, btnManPantry, btnManToBuy;
    TextView tvWelcome;
    ActivityResultLauncher<Intent> customLauncher;
    boolean userAllowed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLogin=findViewById(R.id.btnLogIn);
        btnRegister=findViewById(R.id.btnRegister);
        btnLogout=findViewById(R.id.btnLogOut);
        btnManWeek=findViewById(R.id.btnManageWeek);
        btnManRec=findViewById(R.id.btnManageRecipes);
        btnManPantry=findViewById(R.id.btnManagePantry);
        btnManToBuy=findViewById(R.id.btnManageList);
        tvWelcome=findViewById(R.id.tvWelcome);
        defaultLayout();
        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult resultado) {

                Intent data = resultado.getData();
                if (data != null) {
                    // Recuperar datos editados de la Actividadsec
                    userAllowed = data.getBooleanExtra("allowed",false);
                    if (userAllowed) {
                        //TODO the user should login in the app
                        btnLogin.setVisibility(View.INVISIBLE);
                        btnRegister.setVisibility(View.INVISIBLE);
                        btnLogout.setVisibility(View.VISIBLE);
                        tvWelcome.setVisibility(View.VISIBLE);
                        tvWelcome.setText(getString(R.string.welcome) + " Exp User");
                    }else{
                        defaultLayout();
                    }
                }

            }
        });
    }

    private void defaultLayout() {
        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.INVISIBLE);
        tvWelcome.setVisibility(View.INVISIBLE);
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
        //TODO the user should logout
        defaultLayout();
    }

    public void clickWeek(View v) {
        if(userAllowed){
            Intent intent = new Intent(MainActivity.this, WeekActivity.class);
            customLauncher.launch(intent);
        }
    }

    public void clickRecipes(View v) {
        if(userAllowed) {
            Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
            customLauncher.launch(intent);
        }
    }

    public void clickPantry(View v) {
        if(userAllowed) {
            Intent intent = new Intent(MainActivity.this, PantryActivity.class);
            customLauncher.launch(intent);
        }
    }

    public void clickToBuy(View v) {
        if(userAllowed) {
            Intent intent = new Intent(MainActivity.this, ToBuyActivity.class);
            customLauncher.launch(intent);
        }
    }


}