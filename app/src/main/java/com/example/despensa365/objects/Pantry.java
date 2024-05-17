package com.example.despensa365.objects;

import java.util.ArrayList;
import java.util.Date;

public class Pantry {
    private int id;
    private int userId;
    private int postalCode;
    private String city;
    private String street;
    private int streetNumber;
    private ArrayList<PantryLine> lines;


    public Pantry(int id, int userId, ArrayList<PantryLine> lines, int postalCode, String city, String street, int streetNumber) {
        this.id = id;
        this.userId = userId;
        this.lines = lines;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
    }

    public Pantry() {
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

    public ArrayList<PantryLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<PantryLine> lines) {
        this.lines = lines;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }
}
