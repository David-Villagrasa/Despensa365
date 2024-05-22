package com.example.despensa365.objects;

import java.util.ArrayList;
import java.util.Date;

public class WeeklyPlan {
    private String id;
    private Date startDate;
    private Date endDate;
    private String userId;
    private ArrayList<PlanLine> lines;

    public WeeklyPlan(String id, Date startDate, Date endDate, String userId, ArrayList<PlanLine> lines) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.lines = lines;
    }
    public WeeklyPlan(String id, Date startDate, Date endDate, String userId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.lines = new ArrayList<>();
    }

    public WeeklyPlan() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<PlanLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<PlanLine> lines) {
        this.lines = lines;
    }
}
