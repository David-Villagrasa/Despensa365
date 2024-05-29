package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.example.despensa365.adapters.RecipeAdapter;
import com.example.despensa365.db.DB;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.objects.Recipe;
import com.example.despensa365.objects.RecipeLine;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SelectRecActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private TextView tvHint;
    private EditText etSearch;
    private RecipeAdapter recipeAdapter;
    private Button btnBack, btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ingr);

        recycler = findViewById(R.id.rvIngrList);
        etSearch = findViewById(R.id.etSearchByIng);
        btnBack = findViewById(R.id.btnBackSelectIngr);
        btnSearch = findViewById(R.id.btnSearchSelectIngr);
        tvHint = findViewById(R.id.tvHintIngr);
        tvHint.setText(R.string.hintToUseRecDialog);
        btnBack.setOnClickListener(v -> finish());
        btnSearch.setOnClickListener(v -> searchRecipes());

        setupRecycler();
    }

    private void setupRecycler() {
        recipeAdapter = new RecipeAdapter(this, DB.recipesArrayList, false, false);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(recipeAdapter);
    }

    public void recipeSelected() {
        int pos = recipeAdapter.selectedPosition;
        Recipe recipe = DB.recipesArrayList.get(pos);

        Intent intent = new Intent();
        intent.putExtra("selectedRecipe", recipe);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void searchRecipes() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        if (query.isEmpty()) {
            recipeAdapter.updateList(DB.recipesArrayList);
        } else {
            ArrayList<Recipe> filteredList = (ArrayList<Recipe>) DB.recipesArrayList.stream()
                    .filter(ingredient -> ingredient.getName().toLowerCase().contains(query))
                    .collect(Collectors.toList());
            recipeAdapter.updateList(filteredList);
        }
    }
}
