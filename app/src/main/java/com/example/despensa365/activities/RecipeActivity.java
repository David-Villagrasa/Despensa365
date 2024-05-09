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

import com.example.despensa365.R;
import com.example.despensa365.adapters.RecipeAdapter;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.objects.IngredientType;
import com.example.despensa365.objects.Recipe;
import com.example.despensa365.objects.RecipeLine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class RecipeActivity extends AppCompatActivity {

    private FloatingActionButton delRecipe;
    ActivityResultLauncher<Intent> customLauncher;
    private RecyclerView rvRecipeList;
    private Button btnRecListAdd,btnRecListBack;
    private ArrayList<Recipe> listOfRecipes = new ArrayList<Recipe>();
    final int EDITAR = 200;
    final int ELIMINAR = 300;
    private RecipeAdapter recipeAdapter;
    int posItem;
    //TODO Delete when we can get from db
    public ArrayList<Ingredient> ingredientArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        rvRecipeList = findViewById(R.id.rvRecipeList);
        btnRecListAdd = findViewById(R.id.btnRecListAdd);
        btnRecListBack = findViewById(R.id.btnRecListBack);

        btnRecListAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, SpecificRecipeActivity.class);
            customLauncher.launch(intent);
        });

        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Recipe returnedRecipe = (Recipe) result.getData().getSerializableExtra("RECIPE_DATA");
                    if (returnedRecipe != null) {
                        if (returnedRecipe.getId() == 0) {
                            // New Recipe, assign a new ID
                            int newId = getNextRecipeId();
                            returnedRecipe.setId(newId);
                            listOfRecipes.add(returnedRecipe);
                        } else {
                            // Existing Recipe, update it
                            for (int i = 0; i < listOfRecipes.size(); i++) {
                                if (listOfRecipes.get(i).getId() == returnedRecipe.getId()) {
                                    listOfRecipes.set(i, returnedRecipe);
                                    break;
                                }
                            }
                        }
                        setupRecyclerView(); // Refresh RecyclerView
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
                Recipe recipe = listOfRecipes.get(posItem);
                Intent intent = new Intent(this, SpecificRecipeActivity.class);
                intent.putExtra("RECIPE_DATA", recipe);
                customLauncher.launch(intent);
                recipeAdapter.notifyDataSetChanged();
                break;
            case ELIMINAR:
                listOfRecipes.remove(posItem);
                recipeAdapter.notifyDataSetChanged();
                break;
        }

        return super.onContextItemSelected(item);
    }

    private int getNextRecipeId() {
        int highestId = 0;
        for (Recipe recipe : listOfRecipes) {
            if (recipe.getId() > highestId) {
                highestId = recipe.getId();
            }
        }
        return highestId + 1; // Return one more than the highest ID found
    }

    private void setupRecyclerView() {
        recipeAdapter = new RecipeAdapter(this, listOfRecipes);
        rvRecipeList.setLayoutManager(new LinearLayoutManager(this));
        rvRecipeList.setAdapter(recipeAdapter);
    }

    private void getListOfRecipes() {
        //TODO call to db to get all the recipes that have the id of the actual user
        // Create some example ingredients
        Ingredient flour = new Ingredient(1, "Flour", IngredientType.GRAMS, 500, new Date());
        Ingredient sugar = new Ingredient(2, "Sugar", IngredientType.GRAMS, 300, new Date());
        Ingredient eggs = new Ingredient(3, "Eggs", IngredientType.LITERS, 0.2, new Date());
        ingredientArrayList.add(flour);
        ingredientArrayList.add(sugar);
        ingredientArrayList.add(eggs);
        // Create Recipe
        Recipe cakeRecipe = new Recipe(1, "Cake", "Delicious sponge cake", 123);

        // Create and add Recipe Lines
        RecipeLine flourLine = new RecipeLine();
        flourLine.setIdRecipe(cakeRecipe.getId());
        flourLine.setIdIngredient(flour.getId());
        flourLine.setQuantity(250); // grams of flour

        RecipeLine sugarLine = new RecipeLine();
        sugarLine.setIdRecipe(cakeRecipe.getId());
        sugarLine.setIdIngredient(sugar.getId());
        sugarLine.setQuantity(100); // grams of sugar

        RecipeLine eggLine = new RecipeLine();
        eggLine.setIdRecipe(cakeRecipe.getId());
        eggLine.setIdIngredient(eggs.getId());
        eggLine.setQuantity(3); // number of eggs, using quantity to denote count

        // Adding lines to the recipe
        cakeRecipe.addLine(flourLine);
        cakeRecipe.addLine(sugarLine);
        cakeRecipe.addLine(eggLine);

        listOfRecipes.add(cakeRecipe);
    }


    private void setupListeners() {
        delRecipe.setOnClickListener(v -> {
            // TODO botón de eliminar receta
        });

        btnRecListAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, SpecificRecipeActivity.class);
            customLauncher.launch(intent);
        });

        btnRecListBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void updateRecipeList(Recipe updatedRecipe) {
        listOfRecipes.add(updatedRecipe);
        setupRecyclerView();
    }
}
