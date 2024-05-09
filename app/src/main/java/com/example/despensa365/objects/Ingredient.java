package com.example.despensa365.objects;

import java.io.Serializable;
import java.util.Date;

public class Ingredient implements Serializable {
    private int id;
    private String name;
    private IngredientType type;

    public Ingredient(int id, String name, IngredientType type) {
        this.id = id;
        this.name = name;
        this.type = type;
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
}
