package com.example.despensa365.objects;

import java.util.Date;

public class Ingredient {
    private int id;
    private String name;
    private IngredientType type;
    private double weight;
    private Date expirationDate;

    public Ingredient(int id, String name, IngredientType type, double weight, String password, Date expirationDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.weight = weight;
        this.expirationDate = expirationDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IngredientType getType() {
        return type;
    }

    public void setType(IngredientType type) {
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
