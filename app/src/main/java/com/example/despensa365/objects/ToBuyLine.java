package com.example.despensa365.objects;

public class ToBuyLine {
    private String id;
    private String toBuyId;
    private String ingredientId;
    private double quantity;

    public ToBuyLine(String id, String toBuyId, String ingredientId, double quantity) {
        this.id = id;
        this.toBuyId = toBuyId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }

    public ToBuyLine() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
