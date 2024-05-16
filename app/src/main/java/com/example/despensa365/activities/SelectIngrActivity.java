package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.MainActivity;
import com.example.despensa365.R;
import com.example.despensa365.adapters.IngredientAdapter;
import com.example.despensa365.dialogs.QuantityDateIngredientDialog;
import com.example.despensa365.dialogs.QuantityIngredientDialog;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.objects.PantryLine;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class SelectIngrActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private EditText etSearch;
    private IngredientAdapter ingredientAdapter;
    private boolean needDate = false;
    private Button btnBack, btnSearchSelectIngr;
    private ArrayList<Ingredient> list;
    private int selectedPosition;
    private int posItem;
    private final int ELIMINAR = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ingr);

        recycler = findViewById(R.id.rvIngrList);
        etSearch = findViewById(R.id.etSearchByIng);
        btnBack = findViewById(R.id.btnBackSelectIngr);
        btnSearchSelectIngr = findViewById(R.id.btnSearchSelectIngr);

        list = MainActivity.ingredientArrayList;
        ingredientAdapter = new IngredientAdapter(list, this::openQuantityDialog);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(ingredientAdapter);

        btnBack.setOnClickListener(v -> finish());

        btnSearchSelectIngr.setOnClickListener(v -> searchIngredients());

        needDate = this.getIntent().hasExtra("needDate");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_ingredient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_ingredient_1) {
            Intent intent = new Intent(this, IngredientActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            ingredientAdapter.notifyDataSetChanged();
        }
    }

    private void openQuantityDialog(Ingredient ingredient) {
        if (needDate) {
            QuantityDateIngredientDialog dialog = new QuantityDateIngredientDialog(ingredient, (ing, quantity, date) -> {

                Intent it = new Intent();
                Bundle bd = new Bundle();
                bd.putInt("ingredient", ing.getId());
                bd.putDouble("quantity", quantity);
                it.putExtra("date", date);
                it.putExtras(bd);
                setResult(RESULT_OK, it);

                finish();
            });
            dialog.show(getSupportFragmentManager(), "QuantityIngredientDialog");
        } else {
            QuantityIngredientDialog dialog = new QuantityIngredientDialog(ingredient, (ing, quantity, date) -> {

                Intent it = new Intent();
                Bundle bd = new Bundle();
                bd.putInt("ingredient", ing.getId());
                bd.putDouble("quantity", quantity);
                it.putExtras(bd);
                setResult(RESULT_OK, it);

                finish();
            });
            dialog.show(getSupportFragmentManager(), "QuantityIngredientDialog");
        }

    }

    private void searchIngredients() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        if (query.isEmpty()) {
            ingredientAdapter.updateList(list);
        } else {
            ArrayList<Ingredient> filteredList = (ArrayList<Ingredient>) list.stream()
                    .filter(ingredient -> ingredient.getName().toLowerCase().contains(query))
                    .collect(Collectors.toList());
            ingredientAdapter.updateList(filteredList);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        posItem = item.getGroupId();
        int id = item.getItemId();
        switch (id) {
            case ELIMINAR:
                Ingredient ingredientToRemove = list.get(posItem);
                list.remove(ingredientToRemove);
                ingredientAdapter.updateList(list);

                // TODO: Also remove the ingredient from the database
                break;
        }
        return super.onContextItemSelected(item);
    }

    public interface Callback {
        void dialogOK(Ingredient ing, double quantity, Date date);
    }
}

