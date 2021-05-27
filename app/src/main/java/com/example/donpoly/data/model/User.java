package com.example.donpoly.data.model;

import android.net.Uri;

public class User {

    private String uid;
    private String name;
    private Uri imageUri;


    public User() {
    }

    public User(String uid, String name, Uri imageUri) {
        this.uid = uid;
        this.name = name;
        this.imageUri = imageUri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

}
