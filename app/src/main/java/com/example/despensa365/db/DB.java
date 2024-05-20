package com.example.despensa365.db;

import static com.example.despensa365.methods.DateUtils.getNextMonday;
import static com.example.despensa365.methods.DateUtils.getNextSunday;

import com.example.despensa365.enums.IngredientType;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.objects.PantryLine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DB {

    public static ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    public static FirebaseUser currentUser;
    private static final String TAG = "DB";
    private static FirebaseFirestore db;

    public static void init() {
        db = FirebaseFirestore.getInstance();
    }

    public static FirebaseUser getCurrentUser() {
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

    public static String getNewIngredientId(FirebaseUser user) {
        CollectionReference ingredientsCollection = db.collection("users").document(user.getUid()).collection("ingredients");
        return ingredientsCollection.getId();
    }


    public static void setupDateWeekPlan(@NonNull FirebaseUser user) {
        CollectionReference weekPlanCollection = db.collection("users").document(user.getUid()).collection("weekPlan");

        weekPlanCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    // No documents in the collection
                    Map<String, Object> weekPlanData = new HashMap<>();
                    weekPlanData.put("startDate", getNextMonday());
                    weekPlanData.put("endDate", getNextSunday());

                    weekPlanCollection.document().set(weekPlanData)
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

    public static void savePantry(FirebaseUser currentUser, String postalCode, String city, String street, String streetNumber) {
        if (currentUser != null) {
            // Crear una referencia a un nuevo documento en la colección `pantries` del usuario actual
            DocumentReference newPantryRef = db.collection("users")
                    .document(currentUser.getUid())
                    .collection("pantries")
                    .document();

            // Crear un mapa con la información de la despensa
            Map<String, Object> pantryData = new HashMap<>();
            pantryData.put("postalCode", postalCode);
            pantryData.put("city", city);
            pantryData.put("street", street);
            pantryData.put("streetNumber", streetNumber);

            // Guardar la información de la despensa en Firestore
            newPantryRef.set(pantryData)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Pantry saved successfully with ID: " + newPantryRef.getId()))
                    .addOnFailureListener(e -> Log.w(TAG, "Error saving pantry", e));
        } else {
            Log.w(TAG, "User is not authenticated.");
        }
    }

    public static void checkPantryExists(@NonNull FirebaseUser currentUser, BooleanCallback callback) {
        CollectionReference pantryCollection = db.collection("users")
                .document(currentUser.getUid())
                .collection("pantries");

        pantryCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    callback.onCallback(true); // Existe al menos una despensa
                } else {
                    callback.onCallback(false); // No hay despensas
                }
            } else {
                Log.w(TAG, "Error checking pantry existence", task.getException());
                callback.onCallback(false); // Asumimos que no existe si hay un error
            }
        });
    }

    public static void addUser(String userId, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);

        db.collection("users").document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User added successfully to Firestore!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding user to Firestore", e));
    }

    public static void getIngredients(@NonNull FirebaseUser user) {
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
                        if (typeString.equals("L")) {
                            type = IngredientType.LITERS;
                        } else if (typeString.equals("gr")) {
                            type = IngredientType.GRAMS;
                        } else {
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

    public static void addPantryLine(@NonNull FirebaseUser currentUser, @NonNull PantryLine pantryLine, BooleanCallback callback) {
        CollectionReference pantryLinesCollection = db.collection("users")
                .document(currentUser.getUid())
                .collection("pantries")
                .document(pantryLine.getPantryId())
                .collection("pantryLines");

        DocumentReference newPantryLineRef = pantryLinesCollection.document();

        Map<String, Object> pantryLineData = new HashMap<>();
        pantryLineData.put("ingredient", db.collection("users")
                .document(currentUser.getUid())
                .collection("ingredients")
                .document(pantryLine.getIngredientId()));
        pantryLineData.put("ingredientQuantity", pantryLine.getIngredientQuantity());
        pantryLineData.put("expirationDate", pantryLine.getExpirationDate());

        newPantryLineRef.set(pantryLineData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "PantryLine added successfully with ID: " + newPantryLineRef.getId());
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding PantryLine", e);
                    callback.onCallback(false);
                });
    }

    public static void getPantryId(@NonNull FirebaseUser currentUser, StringCallback callback) {
        CollectionReference pantryCollection = db.collection("users")
                .document(currentUser.getUid())
                .collection("pantries");

        pantryCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Obtener el primer documento de la colección
                    QueryDocumentSnapshot document = (QueryDocumentSnapshot) querySnapshot.getDocuments().get(0);
                    String pantryId = document.getId();
                    callback.onCallback(pantryId);
                } else {
                    Log.d(TAG, "No pantry found for user: " + currentUser.getUid());
                    callback.onCallback(null); // No hay despensas
                }
            } else {
                Log.w(TAG, "Error getting pantry collection", task.getException());
                callback.onCallback(null); // Asumimos que no existe si hay un error
            }
        });
    }

    public static void getAllPantryLines(@NonNull FirebaseUser currentUser, @NonNull String pantryId, PantryLinesCallback callback) {

        CollectionReference pantryLinesCollection = db.collection("users")
                .document(currentUser.getUid())
                .collection("pantries")
                .document(pantryId)
                .collection("pantryLines");

        pantryLinesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<PantryLine> pantryLines = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        PantryLine pantryLine = new PantryLine();
                        pantryLine.setPantryId(pantryId);

                        // Obtener la referencia del ingrediente
                        DocumentReference ingredientRef = document.getDocumentReference("ingredient");
                        if (ingredientRef != null) {
                            pantryLine.setIngredientId(ingredientRef.getId());
                        }

                        // Verificar y obtener la cantidad del ingrediente
                        Double ingredientQuantity = document.getDouble("ingredientQuantity");
                        if (ingredientQuantity != null) {
                            pantryLine.setIngredientQuantity(ingredientQuantity);
                        } else {
                            pantryLine.setIngredientQuantity(0); // O algún valor por defecto
                        }

                        // Verificar y obtener la fecha de expiración
                        Date expirationDate = document.getDate("expirationDate");
                        if (expirationDate != null) {
                            pantryLine.setExpirationDate(expirationDate);
                        } else {
                            pantryLine.setExpirationDate(new Date()); // O algún valor por defecto
                        }

                        pantryLines.add(pantryLine);
                    }
                }

                callback.onCallback(pantryLines);
            } else {
                Log.w(TAG, "Error getting pantry lines collection", task.getException());
                callback.onCallback(new ArrayList<>()); // Devolver una lista vacía en caso de error
            }
        });
    }

    public interface StringCallback {
        void onCallback(String str);
    }

    public interface BooleanCallback {
        void onCallback(boolean success);
    }

    public interface PantryLinesCallback {
        void onCallback(ArrayList<PantryLine> pantryLines);
    }

}
