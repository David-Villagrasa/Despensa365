package com.example.despensa365.activities;

import static com.example.despensa365.methods.Helper.getNormalizedDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.enums.Day;
import com.example.despensa365.R;
import com.example.despensa365.adapters.RecipeAdapter;
import com.example.despensa365.objects.PlanLine;
import com.example.despensa365.objects.Recipe;
import com.example.despensa365.objects.RecipeLine;
import com.example.despensa365.objects.WeeklyPlan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class WeekActivity extends AppCompatActivity {
    final int ELIMINAR = 300;

    private RecyclerView rvRecipesWeek;
    private FloatingActionButton fabAdd;
    private Button btnBackMangWeek, btnDays[];
    private ArrayList<PlanLine> planLines = new ArrayList<>();
    private ArrayList<Recipe> allRecipeList = new ArrayList<>();
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
            btnDays[i].setOnClickListener(v -> loadRecipesForDay(day));
        }

        fabAdd = findViewById(R.id.fabAdd);

        btnBackMangWeek.setOnClickListener(v -> finish());


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeekActivity.this, SelectRecActivity.class);
                customLauncher.launch(intent);
            }
        });

        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult resultado) {

                Intent data = resultado.getData();
                if (data != null) {
                    selectedRecipe = (Recipe) data.getSerializableExtra("selectedRecipe");
                    //TODO create a new line of PlanLine on db
                    allRecipeList.add(selectedRecipe);
                    planLines.add(new PlanLine("", selectedRecipe.getId(), day));
                    loadRecipesForDay(day);
                }

            }
        });

        loadWeeklyPlan();
        getPlanLines();
        setupRecycler();
        loadRecipesForDay(Day.MONDAY); // Default to Monday

    }

    private void getPlanLines() {
        //TODO get the plan from de database
//        Recipe recipe1 = new Recipe(2, "Spaghetti Bolognese", "Classic Italian pasta dish", 0);
//        Recipe recipe2 = new Recipe(3, "Chicken Curry", "Spicy Indian chicken curry", 0);
//        Recipe recipe3 = new Recipe(4, "Beef Tacos", "Delicious Mexican beef tacos", 0);
//        Recipe recipe4 = new Recipe(5, "Vegetable Stir Fry", "Healthy vegetable stir fry", 0);
//        Recipe recipe5 = new Recipe(6, "Pancakes", "Fluffy American pancakes", 0);
//        Recipe recipe6 = new Recipe(7, "Caesar Salad", "Fresh Caesar salad with chicken", 0);
//        Recipe recipe7 = new Recipe(8, "BBQ Ribs", "Tender BBQ pork ribs", 0);
//
//        recipe1.addLine(new RecipeLine(recipe1.getId(), 1, 200));
//        recipe1.addLine(new RecipeLine(recipe1.getId(), 2, 150));
//
//        recipe2.addLine(new RecipeLine(recipe2.getId(), 3, 250));
//        recipe2.addLine(new RecipeLine(recipe2.getId(), 4, 100));
//
//        recipe3.addLine(new RecipeLine(recipe3.getId(), 2, 300));
//        recipe3.addLine(new RecipeLine(recipe3.getId(), 5, 50));
//
//        recipe4.addLine(new RecipeLine(recipe4.getId(), 6, 200));
//        recipe4.addLine(new RecipeLine(recipe4.getId(), 7, 50));
//
//        recipe5.addLine(new RecipeLine(recipe5.getId(), 8, 100));
//        recipe5.addLine(new RecipeLine(recipe5.getId(), 9, 200));
//
//        recipe6.addLine(new RecipeLine(recipe6.getId(), 10, 100));
//        recipe6.addLine(new RecipeLine(recipe6.getId(), 11, 150));
//
//        recipe7.addLine(new RecipeLine(recipe7.getId(), 12, 500));
//        recipe7.addLine(new RecipeLine(recipe7.getId(), 13, 100));
//
//        allRecipeList.add(recipe1);
//        allRecipeList.add(recipe2);
//        allRecipeList.add(recipe3);
//        allRecipeList.add(recipe4);
//        allRecipeList.add(recipe5);
//        allRecipeList.add(recipe6);
//        allRecipeList.add(recipe7);
//
//        planLines.add(new PlanLine(0, recipe1.getId(), Day.MONDAY));
//        planLines.add(new PlanLine(0, recipe2.getId(), Day.MONDAY));
//        planLines.add(new PlanLine(0, recipe3.getId(), Day.MONDAY));
//
//        planLines.add(new PlanLine(0, recipe4.getId(), Day.WEDNESDAY));
//        planLines.add(new PlanLine(0, recipe5.getId(), Day.WEDNESDAY));
//        planLines.add(new PlanLine(0, recipe6.getId(), Day.WEDNESDAY));
//
//        planLines.add(new PlanLine(0, recipe7.getId(), Day.FRIDAY));
//        planLines.add(new PlanLine(0, recipe1.getId(), Day.FRIDAY));
//        planLines.add(new PlanLine(0, recipe2.getId(), Day.FRIDAY));
    }

    private void loadWeeklyPlan() {
        //TODO get the plan from de database
        Calendar calendar = Calendar.getInstance();

        // Suma 7 días a la fecha actual
        calendar.add(Calendar.DAY_OF_YEAR, 7);

        // Devuelve la nueva fecha
        Date sevendays = getNormalizedDate(calendar.getTime());
        weeklyPlan=new WeeklyPlan("", getNormalizedDate(new Date()), sevendays, "", new ArrayList<PlanLine>());
    }

    private void setupRecycler() {
        recipeAdapter = new RecipeAdapter(this, currentDayRecipeList, false, true);
        rvRecipesWeek.setLayoutManager(new LinearLayoutManager(this));
        rvRecipesWeek.setAdapter(recipeAdapter);
    }

    private void loadRecipesForDay(Day selectedDay) {
        day = selectedDay;
        currentDayRecipeList.clear();
        List<String> recipeIdsForDay = planLines.stream()
                .filter(line -> line.getDay() == selectedDay)
                .map(PlanLine::getRecipeId)
                .collect(Collectors.toList());

        for (String recipeId : recipeIdsForDay) {
            for (Recipe recipe : allRecipeList) {
                if (recipe.getId().equals(recipeId)) {
                    currentDayRecipeList.add(recipe);
                    break;
                }
            }
        }

        recipeAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        posItem = item.getGroupId();
        int id = item.getItemId();
        switch (id) {
            case ELIMINAR:
                Recipe recipe = currentDayRecipeList.get(posItem);
                allRecipeList.removeIf(r -> r.getId() == recipe.getId());
                planLines.removeIf(line -> line.getRecipeId() == recipe.getId() && line.getDay() == day);
                loadRecipesForDay(day);

                recipeAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
