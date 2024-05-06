package com.example.despensa365.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ToBuyActivity extends AppCompatActivity {

    private RecyclerView rvToBuy;
    private FloatingActionButton toBuyIngAdd, toBuyIngDel;
    private TextView tvTitleToBuy;
    private Button btnBackToBuy, btnDone, btnAddNeededIngr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_buy_list);

        // Initialize all UI components
        rvToBuy = findViewById(R.id.rvToBuy);
        toBuyIngAdd = findViewById(R.id.toBuyIngAdd);
        toBuyIngDel = findViewById(R.id.toBuyIngDel);
        tvTitleToBuy = findViewById(R.id.tvTitleToBuy);
        btnBackToBuy = findViewById(R.id.btnBackToBuy);
        btnDone = findViewById(R.id.btnDone);
        btnAddNeededIngr = findViewById(R.id.btnAddNeededIngr);

        // Setup listeners for actions
        setupListeners();
    }

    private void setupListeners() {
        btnBackToBuy.setOnClickListener(v -> finish());  // Close the activity

        btnDone.setOnClickListener(v -> {
            // Handle the completion of the shopping list
        });

        btnAddNeededIngr.setOnClickListener(v -> {
            // Logic to add needed ingredients to the shopping list
        });

        toBuyIngAdd.setOnClickListener(v -> {
            // Logic to add a new item to the list
        });

        toBuyIngDel.setOnClickListener(v -> {
            // Logic to delete an item from the list
        });
    }
}
