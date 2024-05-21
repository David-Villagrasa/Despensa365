package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

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
import com.example.despensa365.adapters.RecipeAdapter;
import com.example.despensa365.db.DB;
import com.example.despensa365.objects.Recipe;
import com.example.despensa365.objects.RecipeLine;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> customLauncher;
    private RecyclerView rvRecipeList;
    private Button btnRecListAdd, btnRecListBack;
    private RecipeAdapter recipeAdapter;
    int posItem;
    final int EDITAR = 200;
    final int ELIMINAR = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        rvRecipeList = findViewById(R.id.rvRecipeList);
        btnRecListAdd = findViewById(R.id.btnRecListAdd);
        btnRecListBack = findViewById(R.id.btnRecListBack);

        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Recipe returnedRecipe = (Recipe) result.getData().getSerializableExtra("RECIPE_DATA");
                if (returnedRecipe != null) {
                        if (returnedRecipe.getId().isEmpty()) {
                            // New Recipe
                            DB.addRecipe(DB.currentUser, returnedRecipe, success -> {
                                if (success) {
                                    setupRecyclerView(); // Refresh RecyclerView
                                }
                            });
                        } else {
                            // Existing Recipe
                            DB.updateRecipe(DB.currentUser, returnedRecipe, success -> {
                                if (success) {
                                    setupRecyclerView(); // Refresh RecyclerView
                                }
                            });
                        }

                }
            }
        });

        setupListeners();
        getListOfRecipes();
        setupRecyclerView();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        posItem = item.getGroupId();
        int id = item.getItemId();
        switch (id) {
            case EDITAR:
                Recipe recipe = DB.recipesArrayList.get(posItem);
                Intent intent = new Intent(this, SpecificRecipeActivity.class);
                intent.putExtra("RECIPE_DATA", recipe);
                customLauncher.launch(intent);
                recipeAdapter.notifyDataSetChanged();
                break;
            case ELIMINAR:
                Recipe recipeToDelete = DB.recipesArrayList.get(posItem);
                DB.deleteRecipe(DB.getCurrentUser(), recipeToDelete, success -> {
                    if (success) {
                        recipeAdapter.notifyDataSetChanged();
                    }
                });
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void setupRecyclerView() {
        recipeAdapter = new RecipeAdapter(this, DB.recipesArrayList, true, true);
        rvRecipeList.setLayoutManager(new LinearLayoutManager(this));
        rvRecipeList.setAdapter(recipeAdapter);
    }

    private void getListOfRecipes() {
        DB.getAllRecipes(DB.getCurrentUser(), recipes -> {
            DB.recipesArrayList = recipes;
            setupRecyclerView();
        });
    }

    private void setupListeners() {
        btnRecListAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, SpecificRecipeActivity.class);
            customLauncher.launch(intent);
        });

        btnRecListBack.setOnClickListener(v -> finish());
    }
}
