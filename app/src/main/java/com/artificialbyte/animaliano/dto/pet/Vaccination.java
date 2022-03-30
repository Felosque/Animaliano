package com.artificialbyte.animaliano.dto.pet;

import java.io.Serializable;

public class Vaccination implements Serializable {

    private String name;
    private String date;

    public Vaccination(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public Vaccination() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
