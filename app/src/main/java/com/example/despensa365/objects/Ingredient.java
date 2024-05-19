package com.example.despensa365.objects;

import com.example.despensa365.enums.IngredientType;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private String id;
    private String name;
    private IngredientType type;

    public Ingredient(String id, String name, IngredientType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
    public Ingredient() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
