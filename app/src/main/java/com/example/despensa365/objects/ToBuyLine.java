package com.example.despensa365.objects;

public class ToBuyLine {
    private String toBuyId;
    private String ingredientId;
    private double quantity;

    public ToBuyLine(String toBuyId, String ingredientId, double quantity) {
        this.toBuyId = toBuyId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }

    public String getToBuyId() {
        return toBuyId;
    }

    public void setToBuyId(String toBuyId) {
        this.toBuyId = toBuyId;
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
