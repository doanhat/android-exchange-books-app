package com.example.donpoly.data.model;

import java.util.UUID;

public class User {

    private String id;
    private String displayName;

    public User(String id, String displayName) {
        this.id = UUID.randomUUID().toString();
        this.displayName = displayName;
    }

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
