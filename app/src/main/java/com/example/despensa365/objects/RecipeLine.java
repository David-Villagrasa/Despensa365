package com.example.despensa365.objects;


import java.io.Serializable;

public class RecipeLine implements Serializable {
    private String idRecipe;
    private String idIngredient;
    private double quantity;

    public RecipeLine(){

    }

    public RecipeLine(String idRecipe, String idIngredient, double quantity) {
        this.idRecipe = idRecipe;
        this.idIngredient = idIngredient;
        this.quantity = quantity;
    }

    public String getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(String idRecipe) {
        this.idRecipe = idRecipe;
    }

    public String getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(String idIngredient) {
        this.idIngredient = idIngredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
