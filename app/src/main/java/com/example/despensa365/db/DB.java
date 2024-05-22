package com.example.despensa365.db;

import static com.example.despensa365.methods.DateUtils.getNextMonday;
import static com.example.despensa365.methods.DateUtils.getNextSunday;
import static com.example.despensa365.methods.Helper.getNormalizedDate;

import com.example.despensa365.enums.Day;
import com.example.despensa365.enums.IngredientType;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.objects.PantryLine;
import com.example.despensa365.objects.PlanLine;
import com.example.despensa365.objects.Recipe;
import com.example.despensa365.objects.RecipeLine;
import com.example.despensa365.objects.WeeklyPlan;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DB {

    public static ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    public static ArrayList<PantryLine> pantryLinesArrayList = new ArrayList<>();
    public static ArrayList<RecipeLine> recipeLineArrayList = new ArrayList<>();
    public static ArrayList<Recipe> recipesArrayList = new ArrayList<>();
    public static ArrayList<PlanLine> planLinesArrayList = new ArrayList<>();
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
            DocumentReference newPantryRef = db.collection("users")
                    .document(currentUser.getUid())
                    .collection("pantries")
                    .document();

            Map<String, Object> pantryData = new HashMap<>();
            pantryData.put("postalCode", postalCode);
            pantryData.put("city", city);
            pantryData.put("street", street);
            pantryData.put("streetNumber", streetNumber);

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
                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                }
            } else {
                Log.w(TAG, "Error checking pantry existence", task.getException());
                callback.onCallback(false);
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

    public static void getAllIngredients(@NonNull FirebaseUser user) {
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
                        if (typeString.equals("LITERS")) {
                            type = IngredientType.LITERS;
                        } else if (typeString.equals("GRAMS")) {
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

    public static void addIngredient(@NonNull FirebaseUser currentUser, @NonNull Ingredient ingredient, BooleanCallback callback) {
        CollectionReference ingredientsCollection = db.collection("users")
                .document(currentUser.getUid())
                .collection("ingredients");

        DocumentReference newIngredientRef = ingredientsCollection.document();

        Map<String, Object> ingredientData = new HashMap<>();
        ingredientData.put("name", ingredient.getName());
        ingredientData.put("type", ingredient.getType().toString());

        newIngredientRef.set(ingredientData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Ingredient added successfully with ID: " + newIngredientRef.getId());
                    ingredient.setId(newIngredientRef.getId());
                    ingredientArrayList.add(ingredient);
                    reloadIngredients(currentUser);
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding ingredient", e);
                    callback.onCallback(false);
                });
    }

    public static void deleteIngredient(@NonNull FirebaseUser currentUser, @NonNull Ingredient ingredient, BooleanCallback callback) {
        DocumentReference ingredientRef = db.collection("users")
                .document(currentUser.getUid())
                .collection("ingredients")
                .document(ingredient.getId());

        WriteBatch batch = db.batch();
        ArrayList<Task<Void>> tasks = new ArrayList<>();

        tasks.add(deleteReferencesInCollection(currentUser, ingredient, "pantries", "pantryLines", batch));
        tasks.add(deleteRecipeLineReferences(currentUser, ingredient, batch));

        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        batch.commit()
                                .addOnSuccessListener(aVoid -> {
                                    ingredientRef.delete()
                                            .addOnSuccessListener(aVoid2 -> {
                                                Log.d(TAG, "Ingredient deleted successfully: " + ingredient.getId());

                                                ingredientArrayList.removeIf(ing -> ing.getId().equals(ingredient.getId()));
                                                reloadIngredients(currentUser);
                                                callback.onCallback(true);
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.w(TAG, "Error deleting ingredient", e);
                                                callback.onCallback(false);
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, "Error committing batch deletion", e);
                                    callback.onCallback(false);
                                });
                    } else {
                        Log.w(TAG, "Error deleting references", task.getException());
                        callback.onCallback(false);
                    }
                });
    }


    private static Task<Void> deleteReferencesInCollection(@NonNull FirebaseUser currentUser, @NonNull Ingredient ingredient, String parentCollection, String childCollection, WriteBatch batch) {
        CollectionReference parentCollectionRef = db.collection("users")
                .document(currentUser.getUid())
                .collection(parentCollection);

        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        parentCollectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot parentDoc : task.getResult()) {
                    CollectionReference childCollectionRef = parentCollectionRef
                            .document(parentDoc.getId())
                            .collection(childCollection);

                    childCollectionRef.whereEqualTo("ingredient", db.collection("users")
                                    .document(currentUser.getUid())
                                    .collection("ingredients")
                                    .document(ingredient.getId()))
                            .get()
                            .addOnCompleteListener(childTask -> {
                                if (childTask.isSuccessful()) {
                                    for (QueryDocumentSnapshot childDoc : childTask.getResult()) {
                                        batch.delete(childDoc.getReference());
                                    }
                                    taskCompletionSource.setResult(null);
                                } else {
                                    Log.w(TAG, "Error getting documents: ", childTask.getException());
                                    taskCompletionSource.setException(childTask.getException());
                                }
                            });
                }
            } else {
                Log.w(TAG, "Error getting documents: ", task.getException());
                taskCompletionSource.setException(task.getException());
            }
        });

        return taskCompletionSource.getTask();
    }

    private static Task<Void> deleteRecipeLineReferences(@NonNull FirebaseUser currentUser, @NonNull Ingredient ingredient, WriteBatch batch) {
        CollectionReference recipesCollectionRef = db.collection("users")
                .document(currentUser.getUid())
                .collection("recipes");

        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        recipesCollectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int[] pendingTasksCount = {0};
                int[] completedTasksCount = {0};

                for (QueryDocumentSnapshot recipeDoc : task.getResult()) {
                    CollectionReference recipeLinesCollectionRef = recipesCollectionRef
                            .document(recipeDoc.getId())
                            .collection("recipeLines");

                    pendingTasksCount[0]++;
                    recipeLinesCollectionRef.whereEqualTo("ingredient", db.collection("users")
                                    .document(currentUser.getUid())
                                    .collection("ingredients")
                                    .document(ingredient.getId()))
                            .get()
                            .addOnCompleteListener(lineTask -> {
                                if (lineTask.isSuccessful()) {
                                    for (QueryDocumentSnapshot lineDoc : lineTask.getResult()) {
                                        batch.delete(lineDoc.getReference());
                                    }
                                } else {
                                    Log.w(TAG, "Error getting recipe lines: ", lineTask.getException());
                                }

                                synchronized (completedTasksCount) {
                                    completedTasksCount[0]++;
                                    if (completedTasksCount[0] == pendingTasksCount[0]) {
                                        taskCompletionSource.setResult(null);
                                    }
                                }
                            });
                }

                // Si no hay tareas pendientes, completar la tarea inmediatamente
                if (pendingTasksCount[0] == 0) {
                    taskCompletionSource.setResult(null);
                }
            } else {
                Log.w(TAG, "Error getting recipes: ", task.getException());
                taskCompletionSource.setException(task.getException());
            }
        });

        return taskCompletionSource.getTask();
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
                    pantryLine.setId(newPantryLineRef.getId());
                    pantryLinesArrayList.add(pantryLine);
                    reloadPantryLines(currentUser, pantryLine.getPantryId());
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
                    QueryDocumentSnapshot document = (QueryDocumentSnapshot) querySnapshot.getDocuments().get(0);
                    String pantryId = document.getId();
                    callback.onCallback(pantryId);
                } else {
                    Log.d(TAG, "No pantry found for user: " + currentUser.getUid());
                    callback.onCallback(null);
                }
            } else {
                Log.w(TAG, "Error getting pantry collection", task.getException());
                callback.onCallback(null);
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
                pantryLinesArrayList.clear();  // Clear the existing list before adding new data
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        PantryLine pantryLine = new PantryLine();
                        pantryLine.setId(document.getId());  // Set the document ID
                        pantryLine.setPantryId(pantryId);

                        DocumentReference ingredientRef = document.getDocumentReference("ingredient");
                        if (ingredientRef != null) {
                            pantryLine.setIngredientId(ingredientRef.getId());
                        }

                        Double ingredientQuantity = document.getDouble("ingredientQuantity");
                        if (ingredientQuantity != null) {
                            pantryLine.setIngredientQuantity(ingredientQuantity);
                        } else {
                            pantryLine.setIngredientQuantity(0);
                        }

                        Date expirationDate = document.getDate("expirationDate");
                        if (expirationDate != null) {
                            pantryLine.setExpirationDate(expirationDate);
                        } else {
                            pantryLine.setExpirationDate(new Date());
                        }

                        pantryLinesArrayList.add(pantryLine);
                    }
                }

                callback.onCallback(pantryLinesArrayList);
            } else {
                Log.w(TAG, "Error getting pantry lines collection", task.getException());
                callback.onCallback(new ArrayList<>());  // Return an empty list in case of error
            }
        });
    }


    public static void deletePantryLine(@NonNull FirebaseUser currentUser, @NonNull PantryLine pantryLine, BooleanCallback callback) {
        if (currentUser != null) {
            DocumentReference pantryLineRef = db.collection("users")
                    .document(currentUser.getUid())
                    .collection("pantries")
                    .document(pantryLine.getPantryId())
                    .collection("pantryLines")
                    .document(pantryLine.getId());

            pantryLineRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "PantryLine deleted successfully: " + pantryLine.getId());
                        pantryLinesArrayList.removeIf(line -> line.getId().equals(pantryLine.getId()));
                        reloadPantryLines(currentUser, pantryLine.getPantryId());
                        callback.onCallback(true);
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error deleting PantryLine", e);
                        callback.onCallback(false);
                    });
        } else {
            Log.w(TAG, "User is not authenticated.");
            callback.onCallback(false);
        }
    }
    public static void deleteExpiredPantryLines(@NonNull FirebaseUser currentUser, @NonNull String pantryId, BooleanCallback callback) {
        CollectionReference pantryLinesCollection = db.collection("users")
                .document(currentUser.getUid())
                .collection("pantries")
                .document(pantryId)
                .collection("pantryLines");

        pantryLinesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                WriteBatch batch = db.batch();
                Date today = new Date();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    Date expirationDate = document.getDate("expirationDate");
                    if (expirationDate != null && expirationDate.before(today)) {
                        batch.delete(document.getReference());
                        pantryLinesArrayList.removeIf(line -> line.getId().equals(document.getId()));
                    }
                }

                batch.commit().addOnCompleteListener(batchTask -> {
                    if (batchTask.isSuccessful()) {
                        Log.d(TAG, "Expired pantry lines deleted successfully.");
                        callback.onCallback(true);
                    } else {
                        Log.w(TAG, "Error deleting expired pantry lines", batchTask.getException());
                        callback.onCallback(false);
                    }
                });
            } else {
                Log.w(TAG, "Error getting pantry lines collection", task.getException());
                callback.onCallback(false);
            }
        });
    }

    public static void getAllRecipes(@NonNull FirebaseUser user, RecipesCallback callback) {

        CollectionReference recipesCollection = db.collection("users").document(user.getUid()).collection("recipes");

        recipesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                recipesArrayList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Recipe recipe = document.toObject(Recipe.class);
                    recipe.setId(document.getId());
                    recipesArrayList.add(recipe);
                }
                callback.onCallback(recipesArrayList);
            } else {
                Log.w(TAG, "Error getting recipes collection.", task.getException());
                callback.onCallback(new ArrayList<>());
            }
        });
    }

    public static void addRecipe(@NonNull FirebaseUser currentUser, @NonNull Recipe recipe, BooleanCallback callback) {
        CollectionReference recipesCollection = db.collection("users")
                .document(currentUser.getUid())
                .collection("recipes");

        DocumentReference newRecipeRef = recipesCollection.document();
        recipe.setId(newRecipeRef.getId()); // Set the ID for the recipe object

        Map<String, Object> recipeData = new HashMap<>();
        recipeData.put("name", recipe.getName());
        recipeData.put("description", recipe.getDescription());

        newRecipeRef.set(recipeData)
                .addOnSuccessListener(aVoid -> {
                    recipesArrayList.add(recipe);
                    reloadRecipes(currentUser);
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding recipe", e);
                    callback.onCallback(false);
                });
    }


    public static void deleteRecipe(@NonNull FirebaseUser currentUser, @NonNull Recipe recipe, BooleanCallback callback) {
        DocumentReference recipeRef = db.collection("users")
                .document(currentUser.getUid())
                .collection("recipes")
                .document(recipe.getId());

        WriteBatch batch = db.batch();
        ArrayList<Task<Void>> tasks = new ArrayList<>();

        //tasks.add(deleteReferencesToRecipe(currentUser, recipe.getId(), "weekPlan", batch));

        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        batch.commit()
                                .addOnSuccessListener(aVoid -> {
                                    recipeRef.delete()
                                            .addOnSuccessListener(aVoid2 -> {
                                                Log.d(TAG, "Recipe deleted successfully: " + recipe.getId());

                                                recipesArrayList.removeIf(rec -> rec.getId().equals(recipe.getId()));
                                                reloadRecipes(currentUser);
                                                callback.onCallback(true);
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.w(TAG, "Error deleting recipe", e);
                                                callback.onCallback(false);
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, "Error committing batch deletion", e);
                                    callback.onCallback(false);
                                });
                    } else {
                        Log.w(TAG, "Error deleting references to recipe", task.getException());
                        callback.onCallback(false);
                    }
                });
    }

//    private static Task<Void> deleteReferencesToRecipe(@NonNull FirebaseUser currentUser, @NonNull String recipeId, String childCollection, WriteBatch batch) {
//        CollectionReference recipeLinesCollection = db.collection("users")
//                .document(currentUser.getUid())
//                .collection("recipes")
//                .document(recipeId)
//                .collection(childCollection);
//
//        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
//
//        recipeLinesCollection.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    batch.delete(document.getReference());
//                }
//                taskCompletionSource.setResult(null);
//            } else {
//                Log.w(TAG, "Error getting documents: ", task.getException());
//                taskCompletionSource.setException(task.getException());
//            }
//        });
//
//        return taskCompletionSource.getTask();
//    }
    public static void updateRecipe(@NonNull FirebaseUser currentUser, @NonNull Recipe recipe, BooleanCallback callback) {
        DocumentReference recipeRef = db.collection("users")
                .document(currentUser.getUid())
                .collection("recipes")
                .document(recipe.getId());

        Map<String, Object> recipeData = new HashMap<>();
        recipeData.put("name", recipe.getName());
        recipeData.put("description", recipe.getDescription());

        recipeRef.set(recipeData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    reloadRecipes(currentUser);
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating recipe", e);
                    callback.onCallback(false);
                });
    }


    public static void getAllRecipeLines(@NonNull FirebaseUser user, @NonNull String recipeId, RecipeLinesCallback callback) {
        CollectionReference recipeLinesCollection = db.collection("users")
                .document(user.getUid())
                .collection("recipes")
                .document(recipeId)
                .collection("recipeLines");

        recipeLinesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                recipeLineArrayList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    RecipeLine recipeLine = new RecipeLine();
                    recipeLine.setId(document.getId());
                    recipeLine.setIdRecipe(recipeId);

                    DocumentReference ingredientRef = document.getDocumentReference("ingredient");
                    if (ingredientRef != null) {
                        recipeLine.setIdIngredient(ingredientRef.getId());
                    }

                    recipeLine.setQuantity(document.getDouble("quantity"));
                    recipeLineArrayList.add(recipeLine);
                }
                callback.onCallback(recipeLineArrayList);
            } else {
                Log.w(TAG, "Error getting recipe lines collection.", task.getException());
                callback.onCallback(new ArrayList<>());
            }
        });
    }


    public static void addRecipeLine(@NonNull FirebaseUser currentUser, @NonNull String recipeId, @NonNull RecipeLine recipeLine, BooleanCallback callback) {
        CollectionReference recipeLinesCollection = db.collection("users")
                .document(currentUser.getUid())
                .collection("recipes")
                .document(recipeId)
                .collection("recipeLines");

        DocumentReference newRecipeLineRef = recipeLinesCollection.document();
        recipeLine.setId(newRecipeLineRef.getId()); // Set the ID for the recipe line object

        Map<String, Object> recipeLineData = new HashMap<>();
        recipeLineData.put("ingredient", db.collection("users")
                .document(currentUser.getUid())
                .collection("ingredients")
                .document(recipeLine.getIdIngredient()));
        recipeLineData.put("quantity", recipeLine.getQuantity());

        newRecipeLineRef.set(recipeLineData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    recipeLineArrayList.add(recipeLine);
                    reloadRecipeLines(currentUser, recipeId);
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding recipe line", e);
                    callback.onCallback(false);
                });
    }

    public static void deleteRecipeLine(@NonNull FirebaseUser currentUser, @NonNull RecipeLine recipeLine, BooleanCallback callback) {
        DocumentReference recipeLineRef = db.collection("users")
                .document(currentUser.getUid())
                .collection("recipes")
                .document(recipeLine.getIdRecipe())
                .collection("recipeLines")
                .document(recipeLine.getId());

        recipeLineRef.delete()
                .addOnSuccessListener(aVoid -> {
                    recipeLineArrayList.removeIf(line -> line.getId().equals(recipeLine.getId()));
                    reloadRecipeLines(currentUser, recipeLine.getIdRecipe());
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error deleting recipe line", e);
                    callback.onCallback(false);
                });
    }

    public static void updateRecipeLine(@NonNull FirebaseUser currentUser, @NonNull RecipeLine recipeLine, BooleanCallback callback) {
        DocumentReference recipeLineRef;
        if(recipeLine.getId().isEmpty()){
            recipeLineRef = db.collection("users")
                    .document(currentUser.getUid())
                    .collection("recipes")
                    .document(recipeLine.getIdRecipe())
                    .collection("recipeLines")
                    .document();
        }else{
            recipeLineRef = db.collection("users")
                    .document(currentUser.getUid())
                    .collection("recipes")
                    .document(recipeLine.getIdRecipe())
                    .collection("recipeLines")
                    .document(recipeLine.getId());
        }
        Map<String, Object> recipeLineData = new HashMap<>();
        recipeLineData.put("ingredient", db.collection("users")
                .document(currentUser.getUid())
                .collection("ingredients")
                .document(recipeLine.getIdIngredient()));
        recipeLineData.put("quantity", recipeLine.getQuantity());

        recipeLineRef.set(recipeLineData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    reloadRecipeLines(currentUser, recipeLine.getIdRecipe());
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating recipe line", e);
                    callback.onCallback(false);
                });
    }

    public static void getWeeklyPlan(@NonNull FirebaseUser currentUser, WeeklyPlanCallback callback) {
        CollectionReference weeklyPlanCollection = db.collection("users")
                .document(currentUser.getUid())
                .collection("weekPlan");

        weeklyPlanCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    QueryDocumentSnapshot document = (QueryDocumentSnapshot) querySnapshot.getDocuments().get(0);
                    String weeklyPlanId = document.getId();
                    Timestamp startTimestamp = document.getTimestamp("startDate");
                    Date startDate = getNormalizedDate(startTimestamp.toDate());
                    Timestamp endTimestamp = document.getTimestamp("endDate");
                    Date endDate = getNormalizedDate(endTimestamp.toDate());
                    callback.onCallback(new WeeklyPlan(weeklyPlanId,startDate,endDate,DB.currentUser.getUid()));
                } else {
                    Log.d(TAG, "No weekly plan found for user: " + currentUser.getUid());
                    callback.onCallback(null);
                }
            } else {
                Log.w(TAG, "Error getting weekly plan document", task.getException());
                callback.onCallback(null);
            }
        });
    }
    public static void addPlanLine(@NonNull FirebaseUser currentUser, @NonNull PlanLine planLine, BooleanCallback callback) {
        CollectionReference planLinesCollection = db.collection("users")
                .document(currentUser.getUid())
                .collection("weekPlan")
                .document(planLine.getPlanId())
                .collection("weekPlanLines");

        DocumentReference newPlanLineRef = planLinesCollection.document();

        Map<String, Object> planLineData = new HashMap<>();
        planLineData.put("recipeId", planLine.getRecipeId());
        planLineData.put("day", planLine.getDay().getValue());

        newPlanLineRef.set(planLineData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "PlanLine added successfully with ID: " + newPlanLineRef.getId());
                    planLine.setId(newPlanLineRef.getId());
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding PlanLine", e);
                    callback.onCallback(false);
                });
    }

    public static void getAllPlanLines(@NonNull FirebaseUser currentUser, @NonNull String planId, PlanLinesCallback callback) {
        CollectionReference planLinesCollection = db.collection("users")
                .document(currentUser.getUid())
                .collection("weekPlan")
                .document(planId)
                .collection("weekPlanLines");

        planLinesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<PlanLine> planLines = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        PlanLine planLine = new PlanLine();
                        planLine.setId(document.getId());
                        planLine.setPlanId(planId);
                        DocumentReference recipeRef = document.getDocumentReference("recipe");
                        if (recipeRef != null) {
                            planLine.setRecipeId(recipeRef.getId());
                        }

                        int dayValue = document.getLong("day").intValue();
                        planLine.setDay(convertIntToDay(dayValue));

                        planLines.add(planLine);
                    }
                }

                callback.onCallback(planLines);
            } else {
                Log.w(TAG, "Error getting plan lines collection", task.getException());
                callback.onCallback(new ArrayList<>());
            }
        });
    }

    public static void deletePlanLine(@NonNull FirebaseUser currentUser, @NonNull PlanLine planLine, BooleanCallback callback) {
        DocumentReference planLineRef = db.collection("users")
                .document(currentUser.getUid())
                .collection("weekPlan")
                .document(planLine.getPlanId())
                .collection("weekPlanLines")
                .document(planLine.getId());

        planLineRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "PlanLine deleted successfully: " + planLine.getId());
                    reloadWeekLines(DB.currentUser, planLine.getPlanId());
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error deleting PlanLine", e);
                    callback.onCallback(false);
                });
    }


    private static Day convertIntToDay(int value) {
        switch (value) {
            case 1:
                return Day.MONDAY;
            case 2:
                return Day.TUESDAY;
            case 3:
                return Day.WEDNESDAY;
            case 4:
                return Day.THURSDAY;
            case 5:
                return Day.FRIDAY;
            case 6:
                return Day.SATURDAY;
            case 7:
                return Day.SUNDAY;
            default:
                throw new IllegalArgumentException("Invalid day value: " + value);
        }
    }

    public static void reloadRecipes(@NonNull FirebaseUser user) {
        CollectionReference recipesCollection = db.collection("users").document(user.getUid()).collection("recipes");

        recipesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                recipesArrayList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Recipe recipe = document.toObject(Recipe.class);
                    recipe.setId(document.getId());
                    recipesArrayList.add(recipe);
                }
                Log.d(TAG, "Recipes reloaded successfully.");
            } else {
                Log.w(TAG, "Error reloading recipes collection.", task.getException());
            }
        });
    }
    public static void reloadWeekLines(@NonNull FirebaseUser user, @NonNull String weekPlanId) {
        CollectionReference weekLinesCollection = db.collection("users").document(user.getUid()).collection("weekPlan").document(weekPlanId).collection("weekLines");

        weekLinesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                planLinesArrayList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    PlanLine planLine = new PlanLine();
                    planLine.setId(document.getId());
                    planLine.setPlanId(weekPlanId);
                    DocumentReference recipeRef = document.getDocumentReference("recipe");
                    if (recipeRef != null) {
                        planLine.setRecipeId(recipeRef.getId());
                    }

                    int dayValue = document.getLong("day").intValue();
                    planLine.setDay(convertIntToDay(dayValue));

                    planLinesArrayList.add(planLine);
                }
                Log.d(TAG, "Recipes reloaded successfully.");
            } else {
                Log.w(TAG, "Error reloading recipes collection.", task.getException());
            }
        });
    }

    public static void reloadRecipeLines(@NonNull FirebaseUser user, @NonNull String recipeId) {
        CollectionReference recipeLinesCollection = db.collection("users")
                .document(user.getUid())
                .collection("recipes")
                .document(recipeId)
                .collection("recipeLines");

        recipeLinesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                recipeLineArrayList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    RecipeLine recipeLine = new RecipeLine();
                    recipeLine.setId(document.getId());
                    recipeLine.setIdRecipe(recipeId);
                    recipeLine.setIdIngredient(document.getString("ingredient"));
                    recipeLine.setQuantity(document.getDouble("quantity"));
                    recipeLineArrayList.add(recipeLine);
                }
                Log.d(TAG, "Recipe lines reloaded successfully.");
            } else {
                Log.w(TAG, "Error reloading recipe lines collection.", task.getException());
            }
        });
    }

    public static void reloadIngredients(@NonNull FirebaseUser user) {
        CollectionReference ingredientsCollection = db.collection("users").document(user.getUid()).collection("ingredients");

        ingredientsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ingredientArrayList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String id = document.getId();
                    String name = document.getString("name");
                    String typeString = document.getString("type");
                    IngredientType type;
                    if (typeString.equals("LITERS")) {
                        type = IngredientType.LITERS;
                    } else if (typeString.equals("GRAMS")) {
                        type = IngredientType.GRAMS;
                    } else {
                        type = IngredientType.UNITS;
                    }
                    Ingredient ingredient = new Ingredient(id, name, type);
                    ingredientArrayList.add(ingredient);
                }
                Log.d(TAG, "Ingredients reloaded successfully.");
            } else {
                Log.w(TAG, "Error reloading ingredients collection.", task.getException());
            }
        });
    }

    public static void reloadPantryLines(@NonNull FirebaseUser user, @NonNull String pantryId) {
        CollectionReference pantryLinesCollection = db.collection("users")
                .document(user.getUid())
                .collection("pantries")
                .document(pantryId)
                .collection("pantryLines");

        pantryLinesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                pantryLinesArrayList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    PantryLine pantryLine = new PantryLine();
                    pantryLine.setId(document.getId());
                    pantryLine.setPantryId(pantryId);

                    DocumentReference ingredientRef = document.getDocumentReference("ingredient");
                    if (ingredientRef != null) {
                        pantryLine.setIngredientId(ingredientRef.getId());
                    }

                    Double ingredientQuantity = document.getDouble("ingredientQuantity");
                    if (ingredientQuantity != null) {
                        pantryLine.setIngredientQuantity(ingredientQuantity);
                    } else {
                        pantryLine.setIngredientQuantity(0);
                    }

                    Date expirationDate = document.getDate("expirationDate");
                    if (expirationDate != null) {
                        pantryLine.setExpirationDate(expirationDate);
                    } else {
                        pantryLine.setExpirationDate(new Date());
                    }

                    pantryLinesArrayList.add(pantryLine);
                }
                Log.d(TAG, "Pantry lines reloaded successfully.");
            } else {
                Log.w(TAG, "Error reloading pantry lines collection.", task.getException());
            }
        });
    }

    public interface StringCallback {
        void onCallback(String str);
    }
    public interface BooleanCallback {
        void onCallback(boolean success);
    }
    public interface WeeklyPlanCallback {
        void onCallback(WeeklyPlan weeklyPlan);
    }
    public interface PantryLinesCallback {
        void onCallback(ArrayList<PantryLine> pantryLines);
    }
    public interface PlanLinesCallback {
        void onCallback(ArrayList<PlanLine> planLines);
    }
    public interface RecipesCallback {
        void onCallback(ArrayList<Recipe> recipes);
    }
    public interface RecipeLinesCallback {
        void onCallback(ArrayList<RecipeLine> recipeLines);
    }


}
