package com.example.despensa365.enums;

public enum IngredientType {
    LITERS("L"),
    GRAMS("gr"),
    UNITS("u");

    private final String unit;

    IngredientType(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
