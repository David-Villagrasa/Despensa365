package com.example.despensa365.db;

import static com.example.despensa365.methods.DateUtils.getNextMonday;
import static com.example.despensa365.methods.DateUtils.getNextSunday;

import com.example.despensa365.enums.IngredientType;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.objects.Pantry;
import com.example.despensa365.objects.Recipe;
import com.example.despensa365.objects.ToBuy;
import com.example.despensa365.objects.WeeklyPlan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DB {

    private static final String TAG = "DB";
    private static FirebaseFirestore db;
    public static ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();

    public static void init(){
        db = FirebaseFirestore.getInstance();
    }
    public static FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    public static Ingredient getIngredientById(String id) {
        for (Ingredient ingredient : ingredientArrayList) {
            if (ingredient.getId().equals(id)) {
                return ingredient;
            }
        }
        return null;
    }

    public static String getNewIngredientId(FirebaseUser user){
        CollectionReference ingredientsCollection = db.collection("users").document(user.getUid()).collection("ingredients");
        return ingredientsCollection.getId();
    }


    public static void setupDateWeekPlan(@NonNull FirebaseUser user) {
        CollectionReference weekPlanCollection = db.collection("users").document(user.getUid()).collection("weekPlan");

        weekPlanCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    // No documents in the collection, create "week1"
                    Map<String, Object> weekPlanData = new HashMap<>();
                    weekPlanData.put("startDate", getNextMonday());
                    weekPlanData.put("endDate", getNextSunday());

                    weekPlanCollection.document("week1").set(weekPlanData)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "WeekPlan week1 created successfully."))
                            .addOnFailureListener(e -> Log.w(TAG, "Error creating WeekPlan week1", e));
                } else {
                    // Update the existing document
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference weekPlanRef = weekPlanCollection.document(document.getId());

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("startDate", getNextMonday());
                        updates.put("endDate", getNextSunday());

                        weekPlanRef.update(updates)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "WeekPlan " + document.getId() + " updated successfully."))
                                .addOnFailureListener(e -> Log.w(TAG, "Error updating WeekPlan " + document.getId(), e));
                    }
                }
            } else {
                Log.w(TAG, "Error getting WeekPlan collection.", task.getException());
            }
        });
    }

    public void addUser(String userId, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);

        db.collection("users").document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User added successfully to Firestore!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding user to Firestore", e));
    }
    public static void getIngredients(@NonNull FirebaseUser user){
        CollectionReference ingredientsCollection = db.collection("users").document(user.getUid()).collection("ingredients");

        ingredientsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    // No documents in the collection
                    ingredientArrayList = new ArrayList<>();
                    Log.d(TAG, "No ingredients found.");
                } else {
                    // Get the documents and add them to the ArrayList
                    ingredientArrayList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String name = document.getString("name");
                        String typeString = document.getString("type");
                        IngredientType type;
                        if(typeString.equals("L")){
                            type = IngredientType.LITERS;
                        } else if (typeString.equals("gr")) {
                            type = IngredientType.GRAMS;
                        }else {
                            type = IngredientType.UNITS;
                        }
                        Ingredient ingredient = new Ingredient(id, name, type);
                        ingredientArrayList.add(ingredient);
                    }
                    Log.d(TAG, "Ingredients loaded successfully.");
                }
            } else {
                Log.w(TAG, "Error getting ingredients collection.", task.getException());
            }
        });
    }
    // Method to add Pantry to a User
    public void addPantry(String userId, Pantry pantry) {
        db.collection("users").document(userId)
                .collection("pantry").document(String.valueOf(pantry.getId()))
                .set(pantry)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Pantry added successfully!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding pantry", e));
    }

    // Method to add ToBuy to a User
    public void addToBuy(String userId, ToBuy toBuy) {
        db.collection("users").document(userId)
                .collection("toBuy").document(String.valueOf(toBuy.getId()))
                .set(toBuy)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "ToBuy added successfully!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding toBuy", e));
    }

    // Method to add Recipe to a User
    public void addRecipe(String userId, Recipe recipe) {
        db.collection("users").document(userId)
                .collection("recipes").document(String.valueOf(recipe.getId()))
                .set(recipe)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Recipe added successfully!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding recipe", e));
    }

    // Method to add WeeklyPlan to a User
    public void addWeeklyPlan(String userId, WeeklyPlan weeklyPlan) {
        db.collection("users").document(userId)
                .collection("weeklyPlans").document(String.valueOf(weeklyPlan.getId()))
                .set(weeklyPlan)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "WeeklyPlan added successfully!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding weeklyPlan", e));
    }

    // Additional methods to get, update, delete documents can be added here
}
