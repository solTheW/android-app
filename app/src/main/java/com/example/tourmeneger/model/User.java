package com.example.tourmeneger.model;

public class User {

    private String id;
    private String username;
    private String imageURl;

    public User(String id, String username, String imageURl) {
        this.id = id;
        this.username = username;
        this.imageURl = imageURl;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
    }
}
