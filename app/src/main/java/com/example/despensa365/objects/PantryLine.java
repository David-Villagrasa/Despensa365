package com.example.despensa365.objects;

import java.util.Date;

public class PantryLine {
    private String pantryId;
    private String ingredientId;
    private double ingredientQuantity;
    private Date expirationDate;

    public PantryLine(String pantryId, String ingredientId, double ingredientQuantity, Date expirationDate    ) {
        this.pantryId = pantryId;
        this.ingredientId = ingredientId;
        this.ingredientQuantity = ingredientQuantity;
        this.expirationDate = expirationDate;
    }
    public PantryLine() {
    }

    public String getPantryId() {
        return pantryId;
    }

    public void setPantryId(String pantryId) {
        this.pantryId = pantryId;
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(String ingredientId) {
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
