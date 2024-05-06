package com.example.despensa365.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PantryActivity extends AppCompatActivity {

    private FloatingActionButton pantryDelete, pantryAdd;
    private TextView tvPantryTitle, tvIngre;
    private RecyclerView recyclerView;
    private Button btnBackPantry, btnExpired, btnAddToBuy, btnCleanExpired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        // Initialize TextViews
        tvPantryTitle = findViewById(R.id.tvPantryTitle);
        tvIngre = findViewById(R.id.tvIngre);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Initialize Floating Action Buttons
        pantryDelete = findViewById(R.id.pantryDelete);
        pantryAdd = findViewById(R.id.pantryAdd);

        // Initialize Buttons
        btnBackPantry = findViewById(R.id.btnBackPantry);
        btnExpired = findViewById(R.id.btnExpired);
        btnAddToBuy = findViewById(R.id.btnAddToBuy);
        btnCleanExpired = findViewById(R.id.btnCleanExpired);

        // Set up click listeners for buttons
        setupListeners();
    }

    private void setupListeners() {
        pantryAdd.setOnClickListener(v -> {
            // Add new item logic
        });

        pantryDelete.setOnClickListener(v -> {
            // Delete item logic
        });

        btnBackPantry.setOnClickListener(v -> finish());

        btnExpired.setOnClickListener(v -> {
            // Show expired items logic
        });

        btnAddToBuy.setOnClickListener(v -> {
            // Add to shopping list logic
        });

        btnCleanExpired.setOnClickListener(v -> {
            // Clean up expired items logic
        });
    }
}
