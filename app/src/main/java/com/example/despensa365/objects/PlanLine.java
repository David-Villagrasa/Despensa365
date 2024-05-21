package com.example.despensa365.objects;

import com.example.despensa365.enums.Day;

public class PlanLine {
    private String id;
    private String planId;
    private String recipeId;
    private Day day;

    public PlanLine(String id, String planId, String recipeId, Day day) {
        this.id = id;
        this.planId = planId;
        this.recipeId = recipeId;
        this.day = day;
    }

    public PlanLine() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
