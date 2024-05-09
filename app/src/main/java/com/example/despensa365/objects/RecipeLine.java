package com.example.despensa365.objects;


import java.io.Serializable;

public class RecipeLine implements Serializable {
    private int idRecipe;
    private int idIngredient;
    private double weight;

    public RecipeLine(){

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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
