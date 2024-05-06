package com.example.despensa365.objects;

public enum IngredientType {
    LITERS("L"),
    GRAMS("g");

    private final String unit;

    IngredientType(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
