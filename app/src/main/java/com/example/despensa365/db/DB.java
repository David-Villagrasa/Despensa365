package com.example.despensa365.db;

import com.example.despensa365.objects.Pantry;
import com.example.despensa365.objects.Recipe;
import com.example.despensa365.objects.ToBuy;
import com.example.despensa365.objects.WeeklyPlan;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

public class DB {

    private static final String TAG = "DB";
    private FirebaseFirestore db;

    public DB() {
        db = FirebaseFirestore.getInstance();
    }

    // Method to add a User to Firestore
    public void addUser(String userId, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);

        db.collection("users").document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User added successfully to Firestore!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding user to Firestore", e));
    }

    // Method to add a User
    public void addUser(String userId, Map<String, Object> userData) {
        db.collection("users").document(userId)
                .set(userData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User added successfully!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding user", e));
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
