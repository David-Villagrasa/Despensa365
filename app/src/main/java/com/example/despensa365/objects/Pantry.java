package com.example.despensa365.objects;

import java.util.ArrayList;
import java.util.Date;

public class Pantry {
    private int id;
    private ArrayList<PantryLine> lines;

    public Pantry(int id, ArrayList<PantryLine> lines) {
        this.id = id;
        this.lines = lines;
    }

    public Pantry() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<PantryLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<PantryLine> lines) {
        this.lines = lines;
    }
}
