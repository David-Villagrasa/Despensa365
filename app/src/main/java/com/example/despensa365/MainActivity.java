package com.example.despensa365;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnRegister, btnLogout, btnManWeek, btnManRec, btnManPantry, btnManToBuy;
    TextView tvWelcome;
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

    }

    public void clickLogin(View v) {

    }

    public void clickRegister(View v) {

    }

    public void clickLogout(View v) {

    }

    public void clickWeek(View v) {

    }

    public void clickRecipes(View v) {

    }

    public void clickPantry(View v) {

    }

    public void clickToBuy(View v) {

    }


}