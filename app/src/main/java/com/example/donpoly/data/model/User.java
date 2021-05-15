package com.example.donpoly.data.model;



import com.google.firebase.database.annotations.Nullable;

import java.util.UUID;

public class User {

    private String id;
    private String displayName;
    @Nullable
    private String urlPicture;

    public User() { }

    public User(String uid, String username, String urlPicture) {
        this.id = uid;
        this.displayName = username;
        this.urlPicture = urlPicture;
    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getUsername() { return displayName; }
    public String getUrlPicture() { return urlPicture; }

    // --- SETTERS ---
    public void setUsername(String username) { this.displayName = username; }
    public void setUid(String uid) { this.id = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }

}
