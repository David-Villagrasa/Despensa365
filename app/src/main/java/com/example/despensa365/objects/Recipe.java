package com.example.despensa365.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    private String id = "";
    private String name;
    private String description;
    private String userId;
    private ArrayList<RecipeLine> lines;

    public Recipe(String id, String name, String description, String userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.lines = new ArrayList<>();
    }
    public Recipe() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<RecipeLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<RecipeLine> lines) {
        this.lines = lines;
    }

    public void addLine(RecipeLine line) {
        this.lines.add(line);
    }
}
