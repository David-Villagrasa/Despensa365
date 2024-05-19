package com.example.despensa365.db;

import static com.example.despensa365.methods.DateUtils.getNextMonday;
import static com.example.despensa365.methods.DateUtils.getNextSunday;

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

import java.util.HashMap;
import java.util.Map;

public class DB {

    private static final String TAG = "DB";
    private FirebaseFirestore db;

    public DB() {
        db = FirebaseFirestore.getInstance();
    }

    public void setupDateWeekPlan(FirebaseUser user) {
        CollectionReference weekPlanCollection = db.collection("users").document(user.getUid()).collection("weekPlan");

        weekPlanCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    // No documents in the collection, create "week1"
                    Map<String, Object> weekPlanData = new HashMap<>();
                    weekPlanData.put("startDate", getNextMonday());
                    weekPlanData.put("endDate", getNextSunday());

                    weekPlanCollection.document("week1").set(weekPlanData)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "WeekPlan week1 created successfully!"))
                            .addOnFailureListener(e -> Log.w(TAG, "Error creating WeekPlan week1", e));
                } else {
                    // Update the existing document
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference weekPlanRef = weekPlanCollection.document(document.getId());

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("startDate", getNextMonday());
                        updates.put("endDate", getNextSunday());

                        weekPlanRef.update(updates)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "WeekPlan " + document.getId() + " updated successfully"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error updating WeekPlan " + document.getId(), e));
                    }
                }
            } else {
                Log.w(TAG, "Error getting WeekPlan collection.", task.getException());
            }
        });
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
