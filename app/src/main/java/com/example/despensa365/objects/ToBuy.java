package com.example.despensa365.objects;

import java.util.ArrayList;

public class ToBuy {
    private int id;
    private int userId;
    private String title;
    private ArrayList<ToBuyLine> lines;

    public ToBuy(int id, int userId, String title, ArrayList<ToBuyLine> lines) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.lines = lines;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ToBuyLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<ToBuyLine> lines) {
        this.lines = lines;
    }
}
