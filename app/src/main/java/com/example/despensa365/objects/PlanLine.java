package com.example.despensa365.objects;

import com.example.despensa365.enums.Day;

public class PlanLine {
    private String planId;
    private String recipeId;
    private Day day;

    // Constructor with all fields
    public PlanLine(String planId, String recipeId, Day day) {
        this.planId = planId;
        this.recipeId = recipeId;
        this.day = day;
    }

    // Default constructor
    public PlanLine() {
    }

    // Getter and setter for planId
    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    // Getter and setter for recipeId
    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
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
