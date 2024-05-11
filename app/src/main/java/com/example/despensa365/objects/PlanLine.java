package com.example.despensa365.objects;

import com.example.despensa365.enums.Day;

public class PlanLine {
    private int planId;
    private int recipeId;
    private Day day;

    // Constructor with all fields
    public PlanLine(int planId, int recipeId, Day day) {
        this.planId = planId;
        this.recipeId = recipeId;
        this.day = day;
    }

    // Default constructor
    public PlanLine() {
    }

    // Getter and setter for planId
    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    // Getter and setter for recipeId
    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    // Getter and setter for day
    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
