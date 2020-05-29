package com.example.tourmeneger.model;

public class User {

    private String id;
    private String username;
    private String imageURl;
    private String search;
    private String currgroup;

    public User(String id, String username, String imageURl, String search, String currgroup) {
        this.id = id;
        this.username = username;
        this.imageURl = imageURl;
        this.search = search;
        this.currgroup = currgroup;
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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getCurrgroup() {
        return currgroup;
    }

    public void setCurrgroup(String currgroup) {
        this.currgroup = currgroup;
    }
}
