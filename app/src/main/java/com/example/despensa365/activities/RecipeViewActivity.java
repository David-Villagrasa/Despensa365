package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.example.despensa365.adapters.IngredientsRecipeAdapter;
import com.example.despensa365.objects.Recipe;
import com.example.despensa365.objects.RecipeLine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecipeViewActivity extends AppCompatActivity {
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

        Intent intent = getIntent();
        currentRecipe = (Recipe) intent.getSerializableExtra("recipe");
        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), res -> {
            Intent data = res.getData();
            if (data != null) {
                int idIngr = data.getIntExtra("ingredient",-1);
                double quantity = data.getDoubleExtra("quantity",-1);
                RecipeLine newLine = new RecipeLine(currentRecipe.getId(), idIngr, quantity);
                recipeLines.add(newLine);
                ingredientAdapter.notifyDataSetChanged();
            }
        });

        initViews();

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

        btnCreateRecipe.setVisibility(View.INVISIBLE);
        btnAddIngr.setVisibility(View.INVISIBLE);
        //To let the user select the text
        etNameRecipe.setInputType(InputType.TYPE_NULL);
        etNameRecipe.setFocusable(false);
        etNameRecipe.setTextIsSelectable(true);
        etNameRecipe.setLongClickable(true);
        //To let the user select the text
        etDirectionsRecipe.setInputType(InputType.TYPE_NULL);
        etDirectionsRecipe.setFocusable(false);
        etDirectionsRecipe.setTextIsSelectable(true);
        etDirectionsRecipe.setLongClickable(true);
        setupListeners();
        setupValues();
        setupRecycler();
    }
    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupValues() {
        etNameRecipe.setText(currentRecipe.getName());
        etDirectionsRecipe.setText(currentRecipe.getDescription());
        recipeLines = currentRecipe.getLines();
    }
    private void setupRecycler() {
        if(currentRecipe.getLines() != null){
            recipeLines= currentRecipe.getLines();
        }
        ingredientAdapter = new IngredientsRecipeAdapter(this, recipeLines);
        rvIngreListRecipe.setLayoutManager(new LinearLayoutManager(this));
        rvIngreListRecipe.setAdapter(ingredientAdapter);
    }


}
