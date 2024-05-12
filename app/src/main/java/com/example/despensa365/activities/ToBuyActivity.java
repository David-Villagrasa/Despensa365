package com.example.despensa365.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.MainActivity;
import com.example.despensa365.R;
import com.example.despensa365.adapters.ToBuyAdapter;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.objects.ToBuy;
import com.example.despensa365.objects.ToBuyLine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ToBuyActivity extends AppCompatActivity {
    private ToBuyAdapter toBuyAdapter;
    private ArrayList<ToBuyLine> toBuyLines;
    private RecyclerView rvToBuy;
    private FloatingActionButton toBuyIngAdd, toBuyIngDel;
    private TextView tvTitleToBuy;
    private Button btnBackToBuy, btnDone, btnAddNeededIngr;
    private ToBuy currentToBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_buy_list);

        rvToBuy = findViewById(R.id.rvToBuy);
        toBuyIngAdd = findViewById(R.id.toBuyIngAdd);
        tvTitleToBuy = findViewById(R.id.tvTitleToBuy);
        btnBackToBuy = findViewById(R.id.btnBackToBuy);
        btnDone = findViewById(R.id.btnDone);
        btnAddNeededIngr = findViewById(R.id.btnAddNeededIngr);
        getToBuy();
        setupListeners();
        setupRecyclerView();
    }

    private void getToBuy() {
        //TODO get it from db
        currentToBuy = new ToBuy(1, 0, "Weekly Shopping", new ArrayList<>());
        ArrayList<Ingredient> ingredientArrayList = MainActivity.ingredientArrayList;

        if (ingredientArrayList != null && !ingredientArrayList.isEmpty()) {
            currentToBuy.getLines().add(new ToBuyLine(currentToBuy.getId(), ingredientArrayList.get(0).getId(), 1.0));
            currentToBuy.getLines().add(new ToBuyLine(currentToBuy.getId(), ingredientArrayList.get(1).getId(), 0.5));
            currentToBuy.getLines().add(new ToBuyLine(currentToBuy.getId(), ingredientArrayList.get(2).getId(), 6));
            currentToBuy.getLines().add(new ToBuyLine(currentToBuy.getId(), ingredientArrayList.get(3).getId(), 2.0));
            currentToBuy.getLines().add(new ToBuyLine(currentToBuy.getId(), ingredientArrayList.get(4).getId(), 0.25));
            currentToBuy.getLines().add(new ToBuyLine(currentToBuy.getId(), ingredientArrayList.get(5).getId(), 1.5));
        }

        toBuyLines = currentToBuy.getLines();
    }

    private void setupListeners() {
        btnBackToBuy.setOnClickListener(v -> finish());

        btnDone.setOnClickListener(v -> {
        });

        btnAddNeededIngr.setOnClickListener(v -> {
        });

        toBuyIngAdd.setOnClickListener(v -> {
        });
    }

    private void setupRecyclerView() {
        if(currentToBuy.getLines() != null){
            toBuyLines= currentToBuy.getLines();
        }
        toBuyAdapter = new ToBuyAdapter(this, toBuyLines);
        rvToBuy.setLayoutManager(new LinearLayoutManager(this));
        rvToBuy.setAdapter(toBuyAdapter);
    }
}
