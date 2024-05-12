package com.example.despensa365.objects;

import java.util.ArrayList;
import java.util.Date;

public class WeeklyPlan {
    private int id;
    private Date startDate;
    private Date endDate;
    private int userId;
    private ArrayList<PlanLine> lines;

    public WeeklyPlan(int id, Date startDate, Date endDate, int userId, ArrayList<PlanLine> lines) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.lines = lines;
    }

    public WeeklyPlan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<PlanLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<PlanLine> lines) {
        this.lines = lines;
    }
}
