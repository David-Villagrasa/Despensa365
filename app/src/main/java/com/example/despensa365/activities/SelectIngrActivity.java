package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.MainActivity;
import com.example.despensa365.R;
import com.example.despensa365.adapters.IngredientAdapter;
import com.example.despensa365.dialogs.QuantityIngredientDialog;
import com.example.despensa365.objects.Ingredient;

public class SelectIngrActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private EditText etSearch;
    private IngredientAdapter ingredientAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ingr);

        recycler = findViewById(R.id.rvIngrList);
        etSearch=findViewById(R.id.etSearchByIng);

        ingredientAdapter=new IngredientAdapter(MainActivity.ingredientArrayList, this::openQuantityDialog);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(ingredientAdapter);
        ingredientAdapter.notifyDataSetChanged();
    }

    private void openQuantityDialog(Ingredient ingredient) {
        QuantityIngredientDialog dialog = new QuantityIngredientDialog(ingredient);
        dialog.show(getSupportFragmentManager(), "QuantityIngredientDialog");
    }
}
