package com.example.coursesapp;

public class User {
    private String name;
    private String email;
    private Boolean admin;

    public User() {
    }

//    public User(String name, String email, Boolean admin) {
//        this.name = name;
//        this.email = email;
//        this.admin = admin;
//    }

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


    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

}
