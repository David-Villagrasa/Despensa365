package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.MainActivity;
import com.example.despensa365.R;
import com.example.despensa365.adapters.IngredientsRecipeAdapter;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.objects.Recipe;
import com.example.despensa365.objects.RecipeLine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SpecificRecipeActivity extends AppCompatActivity {
    final int ELIMINAR = 300;
    private FloatingActionButton btnAddIngr;
    private Button btnBack, btnCreateRecipe;
    private EditText etNameRecipe, etDirectionsRecipe;
    private TextView tvRecipeTitle, tvRecipeDirections, tvIngredients;
    private RecyclerView rvIngreListRecipe;
    private Recipe currentRecipe;
    private ArrayList<RecipeLine> recipeLines;
    private IngredientsRecipeAdapter ingredientAdapter;
    int posItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_item);

        initViews();
        handleIntent();
        setupRecyclerView();
    }

    private void initViews() {
        btnAddIngr = findViewById(R.id.recAddIngr);
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

    private void setupRecyclerView() {
        recipeLines= currentRecipe.getLines();
        ingredientAdapter = new IngredientsRecipeAdapter(this, recipeLines);
        rvIngreListRecipe.setLayoutManager(new LinearLayoutManager(this));
        rvIngreListRecipe.setAdapter(ingredientAdapter);
    }

    private List<Ingredient> getIngredients() {
        List<Ingredient> listIngredients = new ArrayList<>();
        for (RecipeLine line:currentRecipe.getLines()) {
            listIngredients.add(MainActivity.SearchIngredient(line.getIdIngredient()));
        }
        return listIngredients;
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnCreateRecipe.setOnClickListener(v -> {
            saveRecipe();
        });
        btnAddIngr.setOnClickListener(v -> {
            addIngredient();
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

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        posItem = item.getGroupId();
        int id = item.getItemId();
        switch (id) {
            case ELIMINAR:
                //TODO When it's removed an item, make sure to remove it from the currentRecipe and from de DB
                recipeLines.remove(posItem);
                currentRecipe.setLines(recipeLines);
                ingredientAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
