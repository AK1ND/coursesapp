package com.example.coursesapp;

import android.net.Uri;

public class User {
    private String name;
    private String email;
    private Uri image;

    public User() {
    }

    public User(String name, String email, Uri image) {
        this.name = name;
        this.email = email;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
