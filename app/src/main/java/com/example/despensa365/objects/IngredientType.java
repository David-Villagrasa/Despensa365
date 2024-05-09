package com.example.despensa365.objects;

public enum IngredientType {
    LITERS("L"),
    GRAMS("gr");

    private final String unit;

    IngredientType(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
