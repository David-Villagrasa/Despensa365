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
    private ArrayList<Recipe> currentDayRecipeList = new ArrayList<>();
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
//                    allRecipeList.add(selectedRecipe);
//                    planLines.add(new PlanLine("0","0", selectedRecipe.getId(), day));
                loadRecipesForDay(day);
            }

        });
        loadWeeklyPlan(()->{});
        loadRecipesForDay(convertIntToDay(getDateOfWeekToday()));
        setupRecycler();

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
        recipeAdapter = new RecipeAdapter(this, currentDayRecipeList, false, true);
        rvRecipesWeek.setLayoutManager(new LinearLayoutManager(this));
        rvRecipesWeek.setAdapter(recipeAdapter);
    }

    private void loadRecipesForDay(Day selectedDay) {
        //TODO: change the color of the other days
        day = selectedDay;
        currentDayRecipeList.clear();
        List<String> recipeIdsForDay = DB.planLinesArrayList.stream()
                .filter(line -> line.getDay() == selectedDay)
                .map(PlanLine::getRecipeId)
                .collect(Collectors.toList());

        for (String recipeId : recipeIdsForDay) {
            for (Recipe recipe : DB.recipesArrayList) {
                if (recipe.getId().equals(recipeId)) {
                    currentDayRecipeList.add(recipe);
                    break;
                }
            }
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        posItem = item.getGroupId();
        int id = item.getItemId();
        switch (id) {
            case ELIMINAR:
//                Recipe recipe = currentDayRecipeList.get(posItem);
//                allRecipeList.removeIf(r -> r.getId() == recipe.getId());
//                planLines.removeIf(line -> line.getRecipeId() == recipe.getId() && line.getDay() == day);
//                loadRecipesForDay(day);

                recipeAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
