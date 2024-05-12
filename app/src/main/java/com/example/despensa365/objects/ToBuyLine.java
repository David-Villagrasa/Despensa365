package com.example.despensa365.objects;

public class ToBuyLine {
    private int toBuyId;
    private int ingredientId;
    private double quantity;

    public ToBuyLine(int toBuyId, int ingredientId, double quantity) {
        this.toBuyId = toBuyId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }

    public int getToBuyId() {
        return toBuyId;
    }

    public void setToBuyId(int toBuyId) {
        this.toBuyId = toBuyId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
