package com.example.despensa365.activities;

import static com.example.despensa365.methods.StaticOnes.getNormalizedDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.MainActivity;
import com.example.despensa365.R;
import com.example.despensa365.adapters.IngredientsPantryAdapter;
import com.example.despensa365.objects.PantryLine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

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
    private int idPantry=1;
    private boolean isSeeingExpired = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);
        //TODO Get the id of this pantry

        tvPantryTitle = findViewById(R.id.tvPantryTitle);
        tvIngre = findViewById(R.id.tvIngre);
        recyclerView = findViewById(R.id.recyclerView);
        pantryAdd = findViewById(R.id.pantryAdd);

        btnBackPantry = findViewById(R.id.btnBackPantry);
        btnExpired = findViewById(R.id.btnExpired);
        btnAddToBuy = findViewById(R.id.btnAddToBuy);
        btnCleanExpired = findViewById(R.id.btnCleanExpired);

        getPantryLines();
        setupRecycler();
        filterNonExpiredLines();
        setupListeners();

        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {

            Intent data = resultado.getData();
            if (data != null) {
                int idIngr = data.getIntExtra("ingredient",-1);
                double quantity = data.getDoubleExtra("quantity",-1);
                Date d = new Date();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    d = data.getSerializableExtra("date", Date.class);
                }
                PantryLine newLine = new PantryLine(idPantry, idIngr, quantity,d);
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
        // Normalized today's date
        Date today = getNormalizedDate(new Date());

        // Create some example pantry lines with normalized dates
        allPantryLines.add(new PantryLine(1, 1, 1000, today)); // 1000 grams of Flour
        allPantryLines.add(new PantryLine(1, 2, 500, today)); // 500 grams of Sugar
        allPantryLines.add(new PantryLine(1, 3, 1, today)); // 1 liter of Eggs
        allPantryLines.add(new PantryLine(1, 4, 2, today)); // 2 liters of Milk
        allPantryLines.add(new PantryLine(1, 5, 200, today)); // 200 grams of Butter
        allPantryLines.add(new PantryLine(1, 6, 1.5, today)); // 1.5 liters of Oil

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -3);
        Date threeDaysAgo = getNormalizedDate(calendar.getTime());

        allPantryLines.add(new PantryLine(1, 1, 750, threeDaysAgo));
        allPantryLines.add(new PantryLine(1, 2, 250, threeDaysAgo));
        allPantryLines.add(new PantryLine(1, 3, 0.5, threeDaysAgo));
        allPantryLines.add(new PantryLine(1, 4, 1, threeDaysAgo));
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
