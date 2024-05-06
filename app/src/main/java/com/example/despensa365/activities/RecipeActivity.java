package com.example.despensa365.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.example.despensa365.objects.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    private FloatingActionButton delRecipe;
    private TextView tvRecipeListTitle;
    private RecyclerView rvRecipeList;
    private Button btnRecListAdd, btnRecListBack;

    private ArrayList<Recipe> listOfRecipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        // Inicialización de los componentes de la UI
        delRecipe = findViewById(R.id.delRecipe);
        tvRecipeListTitle = findViewById(R.id.tvRecipeListTitle);
        rvRecipeList = findViewById(R.id.rvRecipeList);
        btnRecListAdd = findViewById(R.id.btnRecListAdd);
        btnRecListBack = findViewById(R.id.btnRecListBack);

        // Aquí puedes añadir los listeners y cualquier otra lógica inicial que sea necesaria
        setupListeners();
        getListOfRecipes();
    }

    private void getListOfRecipes() {
        //TODO call to db to get all the recipes that have the id of the actual user

    }


    private void setupListeners() {
        delRecipe.setOnClickListener(v -> {
            // TODO botón de eliminar receta
        });

        btnRecListAdd.setOnClickListener(v -> {
            // TODO botón de añadir receta
        });

        btnRecListBack.setOnClickListener(v -> {
            finish();
        });
    }
}
