package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.MainActivity;
import com.example.despensa365.R;
import com.example.despensa365.adapters.IngredientAdapter;
import com.example.despensa365.dialogs.QuantityDateIngredientDialog;
import com.example.despensa365.dialogs.QuantityIngredientDialog;
import com.example.despensa365.objects.Ingredient;

import java.util.Date;

public class SelectIngrActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private EditText etSearch;
    private IngredientAdapter ingredientAdapter;
    private boolean needDate=false;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ingr);

        recycler = findViewById(R.id.rvIngrList);
        etSearch = findViewById(R.id.etSearchByIng);
        btnBack = findViewById(R.id.btnBackSelectIngr);

        ingredientAdapter = new IngredientAdapter(MainActivity.ingredientArrayList, this::openQuantityDialog);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(ingredientAdapter);

        btnBack.setOnClickListener(v -> finish());

        needDate=this.getIntent().hasExtra("needDate");
    }

    private void openQuantityDialog(Ingredient ingredient) {
        if(needDate){
            QuantityDateIngredientDialog dialog = new QuantityDateIngredientDialog(ingredient, (ing, quantity,date)-> {

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
        }else{
            QuantityIngredientDialog dialog = new QuantityIngredientDialog(ingredient, (ing,quantity,date)-> {

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

    public interface Callback {
        void dialogOK(Ingredient ing, double quantity, Date date);
    }
}

