package com.example.despensa365.objects;

import java.util.ArrayList;

public class ToBuy {
    private int id;
    private int userId;
    private String name;
    private ArrayList<ToBuyLine> lines;

    public ToBuy(int id, int userId, String name, ArrayList<ToBuyLine> lines) {
        this.id = id;
        this.userId = userId;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ToBuyLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<ToBuyLine> lines) {
        this.lines = lines;
    }
}
