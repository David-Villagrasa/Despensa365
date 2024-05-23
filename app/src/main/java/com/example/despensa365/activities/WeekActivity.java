package com.example.despensa365.activities;

import static com.example.despensa365.methods.DateUtils.convertIntToDay;
import static com.example.despensa365.methods.DateUtils.getDateOfWeekToday;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.db.DB;
import com.example.despensa365.enums.Day;
import com.example.despensa365.R;
import com.example.despensa365.adapters.RecipeAdapter;
import com.example.despensa365.objects.PlanLine;
import com.example.despensa365.objects.Recipe;
import com.example.despensa365.objects.WeeklyPlan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WeekActivity extends AppCompatActivity {
    final int ELIMINAR = 300;
    private RecyclerView rvRecipesWeek;
    private FloatingActionButton fabAdd;
    private Button btnBackMangWeek, btnDays[];
    private ArrayList<PlanLine> currentDayPlanLineList = new ArrayList<>();
    public Day day = Day.MONDAY;
    private WeeklyPlan weeklyPlan;
    private RecipeAdapter recipeAdapter;
    ActivityResultLauncher<Intent> customLauncher;
    private Recipe selectedRecipe;
    int posItem;

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

        String[] days = getResources().getStringArray(R.array.days_of_week_short);

        for (int i = 0; i < btnDays.length; i++) {
            btnDays[i].setText(days[i]);
            final Day day = Day.values()[i];
            btnDays[i].setOnClickListener(v -> {
                onClick(day);
            });
        }

        fabAdd = findViewById(R.id.fabAdd);

        btnBackMangWeek.setOnClickListener(v -> finish());

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(WeekActivity.this, SelectRecActivity.class);
            customLauncher.launch(intent);
        });

        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {

            Intent data = resultado.getData();
            if (data != null) {
                selectedRecipe = (Recipe) data.getSerializableExtra("selectedRecipe");
                //TODO create a new line of PlanLine on db
                PlanLine planLine = new PlanLine("",weeklyPlan.getId(), selectedRecipe.getId(),day);
                DB.addPlanLine(DB.currentUser,planLine,(v)->{
                    if(v){
                        loadRecipesForDay(day);
                        recipeAdapter.notifyDataSetChanged();
                    }
                });
            }

        });
        loadWeeklyPlan(()->{onClick(convertIntToDay(getDateOfWeekToday()));});
        setupRecycler();
        loadRecipesForDay(convertIntToDay(getDateOfWeekToday()));
    }

    private void onClick(Day day) {
        DB.reloadWeekLines(DB.currentUser, weeklyPlan.getId(), () -> {
            loadRecipesForDay(day);
            recipeAdapter.notifyDataSetChanged();
        });
    }

    private void loadWeeklyPlan(Runnable onLoadedCallback) {
        DB.getWeeklyPlan(DB.currentUser, plan -> {
            weeklyPlan = plan;
            if (onLoadedCallback != null) {
                onLoadedCallback.run();
            }
        });
    }

    private void setupRecycler() {
        recipeAdapter = new RecipeAdapter(this, new ArrayList<>(), false, true); // Cambiado a lista vacía
        rvRecipesWeek.setLayoutManager(new LinearLayoutManager(this));
        rvRecipesWeek.setAdapter(recipeAdapter);
    }

    private void loadRecipesForDay(Day selectedDay) {
        day = selectedDay;
        currentDayPlanLineList.clear();
        List<PlanLine> planLinesForDay = DB.planLinesArrayList.stream()
                .filter(line -> line.getDay() == selectedDay)
                .collect(Collectors.toList());
        currentDayPlanLineList.addAll(planLinesForDay);

        List<Recipe> recipesForDay = currentDayPlanLineList.stream()
                .map(planLine -> DB.recipesArrayList.stream()
                        .filter(recipe -> recipe.getId().equals(planLine.getRecipeId()))
                        .findFirst().orElse(null))
                .collect(Collectors.toList());

        recipeAdapter.updateList(new ArrayList<>(recipesForDay));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        posItem = item.getGroupId();
        int id = item.getItemId();
        switch (id) {
            case ELIMINAR:
                PlanLine planLineToRemove = currentDayPlanLineList.get(posItem);
                DB.deletePlanLine(DB.currentUser, planLineToRemove, success -> {
                    if (success) {
                        onClick(day);
                    }
                });
                break;
        }
        return super.onContextItemSelected(item);
    }
}
