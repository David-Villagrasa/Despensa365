package com.example.despensa365.objects;

import java.util.Date;

public class PantryLine {
    private int pantryId;
    private int ingredientId;
    private double ingredientQuantity;
    private Date expirationDate;

    public PantryLine(int pantryId, int ingredientId, double ingredientQuantity, Date expirationDate    ) {
        this.pantryId = pantryId;
        this.ingredientId = ingredientId;
        this.ingredientQuantity = ingredientQuantity;
        this.expirationDate = expirationDate;
    }
    public PantryLine() {
    }

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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
