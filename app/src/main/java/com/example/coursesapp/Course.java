package com.example.coursesapp;

import com.google.firebase.database.Exclude;

public class Course {

    @Exclude
    private String id;
    private String name;
    private String theme;
    private String description;

    public Course() {

    }

    public Course(String ID, String name, String theme, String description) {
        this.id = ID;
        this.name = name;
        this.theme = theme;
        this.description = description;
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
