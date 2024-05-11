package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.MainActivity;
import com.example.despensa365.R;
import com.example.despensa365.adapters.IngredientAdapter;
import com.example.despensa365.adapters.RecipeAdapter;
import com.example.despensa365.dialogs.QuantityDateIngredientDialog;
import com.example.despensa365.dialogs.QuantityIngredientDialog;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.objects.Recipe;
import com.example.despensa365.objects.RecipeLine;

import java.util.ArrayList;
import java.util.Date;

public class SelectRecActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private EditText etSearch;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> listRecipes= new ArrayList<>();
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ingr);

        recycler = findViewById(R.id.rvIngrList);
        etSearch = findViewById(R.id.etSearchByIng);
        btnBack = findViewById(R.id.btnBackSelectIngr);

        btnBack.setOnClickListener(v -> finish());



        setupList();
        setupRecycler();

    }

    private void setupRecycler() {
        recipeAdapter = new RecipeAdapter(this, listRecipes, false, false);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(recipeAdapter);
    }

    private void setupList() {
        //TODO get data from database
        Recipe cakeRecipe = new Recipe(1, "Cake", "Delicious sponge cake", 123);

        RecipeLine flourLine = new RecipeLine();
        flourLine.setIdRecipe(cakeRecipe.getId());
        flourLine.setIdIngredient(1);
        flourLine.setWeight(250);

        RecipeLine sugarLine = new RecipeLine();
        sugarLine.setIdRecipe(cakeRecipe.getId());
        sugarLine.setIdIngredient(2);
        sugarLine.setWeight(100);

        RecipeLine eggLine = new RecipeLine();
        eggLine.setIdRecipe(cakeRecipe.getId());
        eggLine.setIdIngredient(3);
        eggLine.setWeight(3);

        cakeRecipe.addLine(flourLine);
        cakeRecipe.addLine(sugarLine);
        cakeRecipe.addLine(eggLine);

        listRecipes.add(cakeRecipe);
    }

    public void recipeSelected() {
        int pos = recipeAdapter.selectedPosition;
        Recipe recipe = listRecipes.get(pos);

        Intent intent = new Intent();
        intent.putExtra("selectedRecipe", recipe);
        setResult(RESULT_OK,intent);
        finish();
    }
}
