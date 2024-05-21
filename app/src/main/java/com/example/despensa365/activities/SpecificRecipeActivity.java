package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.example.despensa365.adapters.IngredientsRecipeAdapter;
import com.example.despensa365.db.DB;
import com.example.despensa365.objects.Recipe;
import com.example.despensa365.objects.RecipeLine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SpecificRecipeActivity extends AppCompatActivity {
    final int ELIMINAR = 300;
    private FloatingActionButton btnAddIngr;
    private Button btnBack, btnCreateRecipe;
    private EditText etNameRecipe, etDirectionsRecipe;
    private TextView tvRecipeTitle, tvRecipeDirections, tvIngredients;
    private RecyclerView rvIngreListRecipe;
    private Recipe currentRecipe;
    private ArrayList<RecipeLine> recipeLines = new ArrayList<>();
    private IngredientsRecipeAdapter ingredientAdapter;
    int posItem;

    private ActivityResultLauncher<Intent> customLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_item);

        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), res -> {
            Intent data = res.getData();
            if (data != null) {
                String idIngr = data.getStringExtra("ingredient");
                double quantity = data.getDoubleExtra("quantity", -1);
                RecipeLine newLine = new RecipeLine("0", currentRecipe.getId(), idIngr, quantity);
                recipeLines.add(newLine);
                ingredientAdapter.notifyDataSetChanged();
            }
        });

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

            // Load the recipe lines from the database
            DB.getAllRecipeLines(DB.getCurrentUser(), currentRecipe.getId(), new DB.RecipeLinesCallback() {
                @Override
                public void onCallback(ArrayList<RecipeLine> recipeLinesFromDb) {
                    recipeLines = recipeLinesFromDb;
                    setupRecyclerView();
                }
            });
        } else {
            currentRecipe = new Recipe();  // Create a new empty recipe if no data is passed
        }
    }


    private void populateViews() {
        if (currentRecipe != null) {
            etNameRecipe.setText(currentRecipe.getName());
            etDirectionsRecipe.setText(currentRecipe.getDescription());
        }
    }

    private void loadRecipeLines() {
        DB.getAllRecipeLines(DB.getCurrentUser(), currentRecipe.getId(), recipeLines -> {
            this.recipeLines = recipeLines;
            setupRecyclerView();
        });
    }

    private void setupRecyclerView() {
        ingredientAdapter = new IngredientsRecipeAdapter(this, recipeLines);
        rvIngreListRecipe.setLayoutManager(new LinearLayoutManager(this));
        rvIngreListRecipe.setAdapter(ingredientAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnCreateRecipe.setOnClickListener(v -> saveRecipe());
        btnAddIngr.setOnClickListener(v -> addIngredient());
    }

    private void saveRecipe() {
        currentRecipe.setName(etNameRecipe.getText().toString());
        currentRecipe.setDescription(etDirectionsRecipe.getText().toString());
        currentRecipe.setLines(recipeLines);

        if (currentRecipe.getId() == null || currentRecipe.getId().isEmpty()) {
            // Add new recipe
            DB.addRecipe(DB.getCurrentUser(), currentRecipe, success -> {
                if (success) {
                    for (RecipeLine line : recipeLines) {
                        DB.addRecipeLine(DB.getCurrentUser(), line, lineSuccess -> {
                            // Handle line success if needed
                        });
                    }
                    Intent newIntent = new Intent();
                    newIntent.putExtra("RECIPE_DATA", currentRecipe);
                    setResult(RESULT_OK, newIntent);
                    finish();
                }
            });
        } else {
            // Update existing recipe
            DB.updateRecipe(DB.getCurrentUser(), currentRecipe, success -> {
                if (success) {
                    Intent newIntent = new Intent();
                    newIntent.putExtra("RECIPE_DATA", currentRecipe);
                    setResult(RESULT_OK, newIntent);
                    finish();
                }
            });
        }
    }

    private void addIngredient() {
        Intent intent = new Intent(SpecificRecipeActivity.this, SelectIngrActivity.class);
        customLauncher.launch(intent);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        posItem = item.getGroupId();
        int id = item.getItemId();
        switch (id) {
            case ELIMINAR:
                RecipeLine lineToRemove = recipeLines.get(posItem);
                DB.deleteRecipeLine(DB.getCurrentUser(), lineToRemove, success -> {
                    if (success) {
                        recipeLines.remove(posItem);
                        currentRecipe.setLines(recipeLines);
                        ingredientAdapter.notifyDataSetChanged();
                    }
                });
                break;
        }
        return super.onContextItemSelected(item);
    }
}
