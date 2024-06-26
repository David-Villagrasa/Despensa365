package com.example.despensa365.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.despensa365.R;
import com.example.despensa365.db.DB;

public class RegisterPantryActivity extends AppCompatActivity {

    private EditText etPostalCode, etCity, etStreet, etStreetNumber;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pantry);

        etPostalCode = findViewById(R.id.etPostalCode);
        etCity = findViewById(R.id.etCity);
        etStreet = findViewById(R.id.etStreet);
        etStreetNumber = findViewById(R.id.etStreetNumber);
        btnSave = findViewById(R.id.btnNextRegisterPantry);
        btnCancel = findViewById(R.id.btnBackRegisterPantry);

        btnSave.setOnClickListener(v -> savePantry());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void savePantry() {
        String postalCode = etPostalCode.getText().toString();
        String city = etCity.getText().toString();
        String street = etStreet.getText().toString();
        String streetNumber = etStreetNumber.getText().toString();
        if(postalCode.isEmpty() || city.isEmpty() || street.isEmpty() || streetNumber.isEmpty()) {
            Toast.makeText(this, R.string.fillAll, Toast.LENGTH_SHORT).show();
        }else{
            DB.savePantry(DB.currentUser,postalCode, city, street, streetNumber);

            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        }

    }
}
