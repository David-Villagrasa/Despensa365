package com.example.despensa365.objects;

public class PantryLine {
    private int pantryId;
    private int ingredientId;
    private double ingredientQuantity;

    public PantryLine(int pantryId, int ingredientId, double ingredientQuantity) {
        this.pantryId = pantryId;
        this.ingredientId = ingredientId;
        this.ingredientQuantity = ingredientQuantity;
    }
    public PantryLine() {
    }

    // Getters and Setters
    public int getPantryId() {
        return pantryId;
    }

    public void setPantryId(int pantryId) {
        this.pantryId = pantryId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public double getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(double ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }
}
