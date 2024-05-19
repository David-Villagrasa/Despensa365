package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.despensa365.R;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.enums.IngredientType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IngredientActivity extends AppCompatActivity {

    private Spinner spinnerTypeIngAdd;
    private EditText etNameIngAdd;
    private Button btnBackAddIngredient, btnSaveAddIngredient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        spinnerTypeIngAdd = findViewById(R.id.spinnerTypeIngAdd);
        etNameIngAdd = findViewById(R.id.etNameIngAdd);
        btnBackAddIngredient = findViewById(R.id.btnBackAddIngrendient);
        btnSaveAddIngredient = findViewById(R.id.btnSaveAddIngrendient);

        ArrayAdapter<IngredientType> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, IngredientType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeIngAdd.setAdapter(adapter);

        btnBackAddIngredient.setOnClickListener(v -> finish());

        btnSaveAddIngredient.setOnClickListener(v -> {
            String name = etNameIngAdd.getText().toString();
            IngredientType type = (IngredientType) spinnerTypeIngAdd.getSelectedItem();

            if (!name.isEmpty()) {
                try {
                    // TODO Call the db to get which is the next id to use
                    Ingredient ingredient = new Ingredient("", name, type);
                    // TODO Save ingredient to the list & database

                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this, "Please enter a name and a type", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
