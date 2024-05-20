package com.example.despensa365.activities;

import static com.example.despensa365.methods.Helper.getNormalizedDate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.despensa365.adapters.IngredientsPantryAdapter;
import com.example.despensa365.objects.PantryLine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import com.example.despensa365.db.DB;

public class PantryActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> customLauncher;
    private FloatingActionButton pantryAdd;
    private TextView tvPantryTitle, tvIngre;
    private RecyclerView recyclerView;
    private Button btnBackPantry, btnExpired, btnAddToBuy, btnCleanExpired;
    private IngredientsPantryAdapter ingredientsPantryAdapter;
    private ArrayList<PantryLine> allPantryLines = new ArrayList<>();
    private ArrayList<PantryLine> currentPantryLines = new ArrayList<>();
    final int ELIMINAR = 300;
    int posItem;
    private String pantryId ="";
    private boolean isSeeingExpired = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        tvPantryTitle = findViewById(R.id.tvPantryTitle);
        tvIngre = findViewById(R.id.tvIngre);
        recyclerView = findViewById(R.id.recyclerView);
        pantryAdd = findViewById(R.id.pantryAdd);

        btnBackPantry = findViewById(R.id.btnBackPantry);
        btnExpired = findViewById(R.id.btnExpired);
        btnAddToBuy = findViewById(R.id.btnAddToBuy);
        btnCleanExpired = findViewById(R.id.btnCleanExpired);

        setupRecycler();
        setupListeners();
        //Get the id of this pantry
        DB.getPantryId(DB.currentUser, pantryId -> {
            if (pantryId != null) {
                Log.d("PantryActivity", "Pantry ID: " + pantryId);
                this.pantryId=pantryId;
                getPantryLines();
            } else {
                Log.d("PantryActivity", "No pantry found for the user.");
            }
        });
        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {

            Intent data = resultado.getData();
            if (data != null) {
                String idIngr = data.getStringExtra("ingredient");
                double quantity = data.getDoubleExtra("quantity",-1);
                Date d = new Date();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    d = data.getSerializableExtra("date", Date.class);
                }
                PantryLine newLine = new PantryLine(pantryId, idIngr, quantity,d);
                DB.addPantryLine(DB.currentUser, newLine, success -> {
                    if (success) {
                        Log.d("PantryActivity", "PantryLine added successfully.");
                        // Realiza las acciones necesarias después de añadir la línea de despensa
                    } else {
                        Log.d("PantryActivity", "Failed to add PantryLine.");
                        // Maneja el error de añadir la línea de despensa
                    }
                });
                allPantryLines.add(newLine);
                if (isSeeingExpired) {
                    filterExpiredLines();
                } else {
                    filterNonExpiredLines();
                }
            }

        });
    }

    private void getPantryLines() {
        //TODO get pantry lines from database
        DB.getAllPantryLines(DB.currentUser, pantryId, pantryLines -> {
            if (!pantryLines.isEmpty()) {
                allPantryLines = pantryLines;
                if (isSeeingExpired) {
                    filterExpiredLines();
                } else {
                    filterNonExpiredLines();
                }
            } else {
                Log.d("PantryActivity", "No pantry lines found.");
            }
        });
    }

    private void filterNonExpiredLines() {
        Date today = getNormalizedDate(new Date());
        currentPantryLines = (ArrayList<PantryLine>) allPantryLines.stream()
                .filter(line -> !line.getExpirationDate().before(today))
                .collect(Collectors.toList());

        ingredientsPantryAdapter.pantryLinesList = currentPantryLines;
        ingredientsPantryAdapter.notifyDataSetChanged();
    }

    private void filterExpiredLines() {
        Date today = getNormalizedDate(new Date());
        currentPantryLines = (ArrayList<PantryLine>) allPantryLines.stream()
                .filter(line -> line.getExpirationDate().before(today))
                .collect(Collectors.toList());

        ingredientsPantryAdapter.pantryLinesList = currentPantryLines;
        ingredientsPantryAdapter.notifyDataSetChanged();
    }

    private void setupRecycler() {
        ingredientsPantryAdapter = new IngredientsPantryAdapter(this, currentPantryLines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ingredientsPantryAdapter);
    }

    private void setupListeners() {
        pantryAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectIngrActivity.class);
            intent.putExtra("needDate",true);
            customLauncher.launch(intent);
        });

        btnBackPantry.setOnClickListener(v -> {
            if (isSeeingExpired) {
                isSeeingExpired = false;
                filterNonExpiredLines();
                setupRecycler();
                toggleButtonVisibility();
            } else {
                finish();
            }
        });

        btnExpired.setOnClickListener(v -> {
            isSeeingExpired = true;
            filterExpiredLines();
            setupRecycler();
            toggleButtonVisibility();
        });

        btnAddToBuy.setOnClickListener(v -> {
        });

        btnCleanExpired.setOnClickListener(v -> {
            cleanExpiredItems();
        });
    }

    private void cleanExpiredItems() {
        Date today = getNormalizedDate(new Date());
        allPantryLines.removeIf(line -> line.getExpirationDate().before(today));
        if (isSeeingExpired) {
            filterExpiredLines();
        } else {
            filterNonExpiredLines();
        }
        ingredientsPantryAdapter.notifyDataSetChanged();
    }

    private void toggleButtonVisibility() {
        if (isSeeingExpired) {
            tvIngre.setText(R.string.pantryIngrExp);
            btnExpired.setVisibility(View.INVISIBLE);
            btnAddToBuy.setVisibility(View.VISIBLE);
            btnCleanExpired.setVisibility(View.VISIBLE);
            pantryAdd.setVisibility(View.INVISIBLE);
        } else {
            tvIngre.setText(R.string.pantryIngr);
            btnExpired.setVisibility(View.VISIBLE);
            btnAddToBuy.setVisibility(View.INVISIBLE);
            btnCleanExpired.setVisibility(View.INVISIBLE);
            pantryAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        posItem = item.getGroupId();
        int id = item.getItemId();
        switch (id) {
            case ELIMINAR:
                PantryLine selectedLine = currentPantryLines.get(posItem);
                allPantryLines.removeIf(line -> line.getIngredientId() == selectedLine.getIngredientId() &&
                        line.getPantryId() == selectedLine.getPantryId() &&
                        line.getExpirationDate().equals(selectedLine.getExpirationDate()));

                if (isSeeingExpired) {
                    filterExpiredLines();
                } else {
                    filterNonExpiredLines();
                }
                ingredientsPantryAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
