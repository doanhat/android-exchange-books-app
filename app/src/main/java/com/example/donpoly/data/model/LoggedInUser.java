package com.example.donpoly.data.model;

import android.net.Uri;

import java.util.List;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String email;
    private Uri imageUri;
    private List<Proposition> propositions;
    private List<Proposition> commands;

    public LoggedInUser(String userId, String displayName, String email, Uri imageUri, List<Proposition> propositions, List<Proposition> commands) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.imageUri = imageUri;
        this.propositions = propositions;
        this.commands = commands;
    }


    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail(){
        return email;
    }

    public Uri getImageUri(){
        return imageUri;
    }

    public List<Proposition> getPropositions(){
        return propositions;
    }

    public List<Proposition> getCommands(){
        return commands;
    }

}
