package com.example.despensa365.activities;

import static com.example.despensa365.methods.DateUtils.convertIntToDay;
import static com.example.despensa365.methods.DateUtils.getDateOfWeekToday;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

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
import com.example.despensa365.objects.RecipeLine;
import com.example.despensa365.objects.WeeklyPlan;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
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
        for (int i = 0; i < btnDays.length; i++) {
            btnDays[i].setText(days[i]);
            final Day day = Day.values()[i];
            btnDays[i].setOnClickListener(v -> {
                onClick(day);
            });
        }
    }

    private void onClick(Day day) {
        resetColors();
        btnDays[day.getValue()-1].setBackgroundColor(getResources().getColor(R.color.dark_green));
        DB.reloadWeekLines(DB.currentUser, weeklyPlan.getId(), () -> {
            loadRecipesForDay(day);
            recipeAdapter.notifyDataSetChanged();
        });
    }

    private void resetColors(){
        for (int i = 0; i < btnDays.length; i++) {
            btnDays[i].setBackgroundColor(getResources().getColor(R.color.dark_red));
        }
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
        recipeAdapter = new RecipeAdapter(this, new ArrayList<>(), false, true);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_week_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==(R.id.action_done_recipes)) {
            handleDoneRecipes();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void handleDoneRecipes() {
        List<PlanLine> planLinesForToday = currentDayPlanLineList;
        List<RecipeLine> allRecipeLines = new ArrayList<>();
        List<Task<Void>> tasks = new ArrayList<>();

        for (PlanLine planLine : planLinesForToday) {
            TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
            tasks.add(taskCompletionSource.getTask());

            DB.getAllRecipeLines(DB.getCurrentUser(), planLine.getRecipeId(), recipeLines -> {
                allRecipeLines.addAll(recipeLines);
                taskCompletionSource.setResult(null);
            });
        }

        Tasks.whenAll(tasks).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DB.checkAndConsumeIngredients(DB.getCurrentUser(), allRecipeLines, success -> {
                    if (success) {
                        runOnUiThread(() -> {
                            Toast.makeText(WeekActivity.this, R.string.ingredientsUpdated, Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(WeekActivity.this, R.string.notEnoughIngredients, Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                runOnUiThread(() -> Toast.makeText(WeekActivity.this, "Error", Toast.LENGTH_SHORT).show());
            }
        });
    }



}
