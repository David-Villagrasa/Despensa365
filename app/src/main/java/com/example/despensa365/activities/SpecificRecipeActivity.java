package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.example.despensa365.adapters.IngredientAdapter;
import com.example.despensa365.objects.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SpecificRecipeActivity extends AppCompatActivity {
    private FloatingActionButton btnAddIngr, btnRemoveIngr;
    private Button btnBack, btnCreateRecipe;
    private EditText etNameRecipe, etDirectionsRecipe;
    private TextView tvRecipeTitle, tvRecipeDirections, tvIngredients;
    private RecyclerView rvIngreListRecipe;
    private Recipe currentRecipe;
    private IngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_item);

        initViews();
        handleIntent();
    }

    private void initViews() {
        btnAddIngr = findViewById(R.id.recAddIngr);
        btnRemoveIngr = findViewById(R.id.recRemoveIngr);
        btnBack = findViewById(R.id.btnBackRecepeItem);
        btnCreateRecipe = findViewById(R.id.btnCreateRecipe);
        etNameRecipe = findViewById(R.id.etNameRecipe);
        etDirectionsRecipe = findViewById(R.id.etDirectionsRecipe);
        tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        tvRecipeDirections = findViewById(R.id.tvRecipeDirections);
        tvIngredients = findViewById(R.id.tvIngredients);
        rvIngreListRecipe = findViewById(R.id.rvIngreListRecipe);

        setupListeners();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("RECIPE_DATA")) {
            currentRecipe = (Recipe) intent.getSerializableExtra("RECIPE_DATA");
            populateViews();
        } else {
            currentRecipe = new Recipe();  // Create a new empty recipe if no data is passed
        }
    }

    private void populateViews() {
        if (currentRecipe != null) {
            etNameRecipe.setText(currentRecipe.getName());
            etDirectionsRecipe.setText(currentRecipe.getDescription());
            // TODO setup the RecyclerView with the ingredients here
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnCreateRecipe.setOnClickListener(v -> {
            saveRecipe();
        });
        btnAddIngr.setOnClickListener(v -> {
            addIngredient();
        });
        btnRemoveIngr.setOnClickListener(v -> {
            removeIngredient();
        });
    }

    private void saveRecipe() {
        // TODO recipe to database or update it
        currentRecipe.setName(etNameRecipe.getText().toString());
        currentRecipe.setDescription(etDirectionsRecipe.getText().toString());
        // TODO update the ingredients list here currentRecipe.setLines();
        Intent newIntent = new Intent();
        newIntent.putExtra("RECIPE_DATA", currentRecipe);

        setResult(RESULT_OK, newIntent);
        finish();
    }

    private void addIngredient() {
        // TODO new ingredient to the recipe
    }

    private void removeIngredient() {
        // TODO remove an ingredient from the recipe
    }
}
