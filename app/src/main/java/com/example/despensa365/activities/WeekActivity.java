package com.example.despensa365.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WeekActivity extends AppCompatActivity {

    private RecyclerView rvRecipesWeek;
    private FloatingActionButton fabAdd, fabDelete;
    private Button btnBackMangWeek, btnDays[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_management);

        rvRecipesWeek = findViewById(R.id.rvRecipesWeek);

        btnBackMangWeek = findViewById(R.id.btnBackMangWeek);
        btnDays = new Button[7];
        btnDays[0] = findViewById(R.id.btnMonday);
        btnDays[1] = findViewById(R.id.btnTuesday);
        btnDays[2] = findViewById(R.id.btnWednesday);
        btnDays[3] = findViewById(R.id.btnThursday);
        btnDays[4] = findViewById(R.id.btnFriday);
        btnDays[5] = findViewById(R.id.btnSaturday);
        btnDays[6] = findViewById(R.id.btnSunday);

        // Retrieve the array of day names from resources
        String[] days = getResources().getStringArray(R.array.days_of_week_short);

        // Set the text for each button based on the day names
        for (int i = 0; i < btnDays.length; i++) {
            btnDays[i].setText(days[i]);
        }

        fabAdd = findViewById(R.id.fabAdd);
        fabDelete = findViewById(R.id.fabDelete);

        btnBackMangWeek.setOnClickListener(v -> finish());

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic for adding a new recipe to the day
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic for deleting a selected recipe from the day
            }
        });

    }

    private void loadRecipesForDay(String day) {
        // Logic to load recipes for a specific day, possibly updating the adapter for rvRecipesWeek
    }
}
