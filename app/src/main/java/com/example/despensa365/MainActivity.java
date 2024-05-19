package com.example.despensa365;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.despensa365.activities.LoginActivity;
import com.example.despensa365.activities.PantryActivity;
import com.example.despensa365.activities.RecipeActivity;
import com.example.despensa365.activities.RegisterActivity;
import com.example.despensa365.activities.ToBuyActivity;
import com.example.despensa365.activities.WeekActivity;
import com.example.despensa365.db.DB;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.enums.IngredientType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {
    public FirebaseAuth firebaseAuth;
    private DB db;
    //TODO Delete when we can get from db
    public static ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    Button btnLogin, btnRegister, btnLogout, btnManWeek, btnManRec, btnManPantry, btnManToBuy;
    TextView tvWelcome;
    ActivityResultLauncher<Intent> customLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin=findViewById(R.id.btnLogIn);
        btnRegister=findViewById(R.id.btnRegister);
        btnLogout=findViewById(R.id.btnLogOut);
        btnManWeek=findViewById(R.id.btnManageWeek);
        btnManRec=findViewById(R.id.btnManageRecipes);
        btnManPantry=findViewById(R.id.btnManagePantry);
        btnManToBuy=findViewById(R.id.btnManageList);
        tvWelcome=findViewById(R.id.tvWelcome);
        firebaseAuth= FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null){
            defaultLayout();
        }else{
            loggedLayout();
        }
        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(firebaseAuth.getCurrentUser()==null){
                defaultLayout();
            }else{
                loggedLayout();
            }
        });
        db = new DB();
        defaultIngredientsTEST();
    }

    private void defaultIngredientsTEST() {
        Ingredient flour = new Ingredient(1, "Flour", IngredientType.GRAMS);
        Ingredient sugar = new Ingredient(2, "Sugar", IngredientType.GRAMS);
        Ingredient eggs = new Ingredient(3, "Eggs", IngredientType.UNITS);
        Ingredient milk = new Ingredient(4, "Milk", IngredientType.LITERS);
        Ingredient butter = new Ingredient(5, "Butter", IngredientType.GRAMS);
        Ingredient oil = new Ingredient(6, "Oil", IngredientType.LITERS);
        ingredientArrayList.add(flour);
        ingredientArrayList.add(sugar);
        ingredientArrayList.add(eggs);
        ingredientArrayList.add(milk);
        ingredientArrayList.add(butter);
        ingredientArrayList.add(oil);
    }

    private void defaultLayout() {
        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.INVISIBLE);
        tvWelcome.setVisibility(View.INVISIBLE);
    }

    private void loggedLayout() {
        btnLogin.setVisibility(View.INVISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);
        btnLogout.setVisibility(View.VISIBLE);
        tvWelcome.setVisibility(View.VISIBLE);
        FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
        String username = userFirebase.getDisplayName();
        tvWelcome.setText(String.format("%s %s", getString(R.string.welcome), username));
        db.setupDateWeekPlan(firebaseAuth.getCurrentUser());
    }

    public void clickLogin(View v) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        customLauncher.launch(intent);
    }

    public void clickRegister(View v) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        customLauncher.launch(intent);
    }

    public void clickLogout(View v) {
        firebaseAuth.signOut();
        defaultLayout();
    }

    public void clickWeek(View v) {
        if(firebaseAuth.getCurrentUser()!=null){
            Intent intent = new Intent(MainActivity.this, WeekActivity.class);
            customLauncher.launch(intent);
        }else{
            Toast.makeText(this, R.string.hintMainActivity, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickRecipes(View v) {
        if(firebaseAuth.getCurrentUser()!=null) {
            Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
            customLauncher.launch(intent);
        }else{
            Toast.makeText(this, R.string.hintMainActivity, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickPantry(View v) {
        if(firebaseAuth.getCurrentUser()!=null) {
            Intent intent = new Intent(MainActivity.this, PantryActivity.class);
            customLauncher.launch(intent);
        }else{
            Toast.makeText(this, R.string.hintMainActivity, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickToBuy(View v) {
        if(firebaseAuth.getCurrentUser()!=null) {
            Intent intent = new Intent(MainActivity.this, ToBuyActivity.class);
            customLauncher.launch(intent);
        }else{
            Toast.makeText(this, R.string.hintMainActivity, Toast.LENGTH_SHORT).show();
        }
    }


    public static Optional<Ingredient> SearchIngredient(int id){
        Optional<Ingredient> ingredient = Optional.empty();
        for (Ingredient i: ingredientArrayList) {
            if(i.getId() == id){
                ingredient = Optional.of(i);
                return ingredient;
            }
        }
        return ingredient;
    }


}