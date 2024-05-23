package com.example.despensa365.objects;

import java.util.ArrayList;

public class ToBuy {
    private String id;
    private String userId;
    private String title;
    private ArrayList<ToBuyLine> lines;

    public ToBuy(String id, String userId, String title, ArrayList<ToBuyLine> lines) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.lines = lines;
    }
    public ToBuy(String id, String userId, String title) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.lines = new ArrayList<>();
    }
    public ToBuy() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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
