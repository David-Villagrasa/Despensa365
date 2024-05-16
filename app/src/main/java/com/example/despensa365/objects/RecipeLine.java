package com.example.despensa365.objects;


import java.io.Serializable;

public class RecipeLine implements Serializable {
    private int idRecipe;
    private int idIngredient;
    private double quantity;

    public RecipeLine(){

    }

    public RecipeLine(int idRecipe, int idIngredient, double quantity) {
        this.idRecipe = idRecipe;
        this.idIngredient = idIngredient;
        this.quantity = quantity;
    }

    public int getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(int idRecipe) {
        this.idRecipe = idRecipe;
    }

    public int getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(int idIngredient) {
        this.idIngredient = idIngredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
