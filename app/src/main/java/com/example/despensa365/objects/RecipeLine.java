package com.example.despensa365.objects;

import java.io.Serializable;

public class RecipeLine implements Serializable {
    private String id;
    private String idRecipe;
    private String idIngredient;
    private double quantity;

    public RecipeLine(String id, String idRecipe, String idIngredient, double quantity) {
        this.id = id;
        this.idRecipe = idRecipe;
        this.idIngredient = idIngredient;
        this.quantity = quantity;
    }

    public RecipeLine() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
