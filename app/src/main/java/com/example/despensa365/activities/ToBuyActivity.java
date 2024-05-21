package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.example.despensa365.adapters.BoughtAdapter;
import com.example.despensa365.adapters.ToBuyAdapter;
import com.example.despensa365.objects.ToBuy;
import com.example.despensa365.objects.ToBuyLine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ToBuyActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> customLauncher;
    private ToBuyAdapter toBuyAdapter;
    private BoughtAdapter boughtAdapter;
    private ArrayList<ToBuyLine> toBuyLines;
    private RecyclerView rvToBuy;
    private FloatingActionButton toBuyIngAdd;
    private TextView tvTitleToBuy, tvHint;
    private Button btnBackToBuy, btnDone, btnAddNeededIngr;
    private ToBuy currentToBuy;
    private boolean isBought=false;
    private int posItem;


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
        tvHint = findViewById(R.id.tvHintToBuy);
        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {

            Intent data = resultado.getData();
            if (data != null) {
                String idIngr = data.getStringExtra("ingredient");
                double quantity = data.getDoubleExtra("quantity",-1);
                ToBuyLine newLine = new ToBuyLine("0",currentToBuy.getId(), idIngr, quantity);
                toBuyLines.add(newLine);
                toBuyAdapter.updateList(toBuyLines);
                toBuyAdapter.notifyDataSetChanged();
            }

        });
        getToBuyLines();
        setupListeners();
        setupRecyclerView();
    }

    private void getToBuyLines() {
        //TODO get it from db
        toBuyLines=new ArrayList<ToBuyLine>();
//        currentToBuy = new ToBuy(1, 0, "Weekly Shopping", new ArrayList<>());
//        ArrayList<Ingredient> ingredientArrayList = MainActivity.ingredientArrayList;
//
//        if (ingredientArrayList != null && !ingredientArrayList.isEmpty()) {
//            currentToBuy.getLines().add(new ToBuyLine(currentToBuy.getId(), ingredientArrayList.get(0).getId(), 1.0));
//            currentToBuy.getLines().add(new ToBuyLine(currentToBuy.getId(), ingredientArrayList.get(1).getId(), 0.5));
//            currentToBuy.getLines().add(new ToBuyLine(currentToBuy.getId(), ingredientArrayList.get(2).getId(), 6));
//            currentToBuy.getLines().add(new ToBuyLine(currentToBuy.getId(), ingredientArrayList.get(3).getId(), 2.0));
//            currentToBuy.getLines().add(new ToBuyLine(currentToBuy.getId(), ingredientArrayList.get(4).getId(), 0.25));
//            currentToBuy.getLines().add(new ToBuyLine(currentToBuy.getId(), ingredientArrayList.get(5).getId(), 1.5));
//        }
//
//        toBuyLines = currentToBuy.getLines();
    }

    private void setupListeners() {
        btnBackToBuy.setOnClickListener(v -> {
            if (isBought) {
                isBought=false;
                btnDone.setVisibility(View.VISIBLE);
                btnAddNeededIngr.setVisibility(View.VISIBLE);
                setupRecyclerView();
                tvHint.setText("");
            }else{
                finish();
            }
        });

        btnDone.setOnClickListener(v -> {
            setupAsBought();
        });

        btnAddNeededIngr.setOnClickListener(v -> {
            //check what ingredients user has in his pantry and which & how much user need with the lines of the recipes of the weeklyplan
        });

        toBuyIngAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectIngrActivity.class);
            customLauncher.launch(intent);
        });
    }

    private void setupAsBought() {
        isBought=true;
        btnDone.setVisibility(View.INVISIBLE);
        btnAddNeededIngr.setVisibility(View.INVISIBLE);
        ArrayList<ToBuyLine> lines = new ArrayList<>();
        for (int i = 0; i < toBuyLines.size(); i++) {
            if(toBuyAdapter.selected[i]){
                lines.add(toBuyLines.get(i));
            }
        }
        setupRecyclerViewBought(lines);
        tvHint.setText(R.string.hintWhenBought);
    }

    private void setupRecyclerView() {
        toBuyAdapter = new ToBuyAdapter(this, toBuyLines);
        rvToBuy.setLayoutManager(new LinearLayoutManager(this));
        rvToBuy.setAdapter(toBuyAdapter);
    }

    private void setupRecyclerViewBought(ArrayList<ToBuyLine> lines) {
        boughtAdapter = new BoughtAdapter(this, lines);
        rvToBuy.setLayoutManager(new LinearLayoutManager(this));
        rvToBuy.setAdapter(boughtAdapter);
    }
    private final int ELIMINAR = 300;

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        posItem = item.getGroupId();
        int id = item.getItemId();
        switch (id) {
            case ELIMINAR:
                ToBuyLine ingredientToRemove = toBuyLines.get(posItem);
                toBuyLines.remove(ingredientToRemove);
                toBuyAdapter.updateList(toBuyLines);

                // TODO: Also remove the toBuyLine from the database
                break;
        }
        return super.onContextItemSelected(item);
    }
}
