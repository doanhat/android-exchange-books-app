package com.example.donpoly.data.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.UUID;

@IgnoreExtraProperties
public class Message {
    private String id;
    private User sender;
    private String content;
    private Date dateCreated;
    private String urlImage;

    public Message() { }

    public Message(String id, User sender, String content) {
        this.id = id;
        this.sender = sender;
        this.content = content;
    }
    public Message(String id, String urlImage, User userSender) {
        this.id = id;
        this.urlImage = urlImage;
        this.sender = userSender;
    }
    // --- GETTERS ---
    public User getSender() {
        return sender;
    }
    public String getContent() {
        return content;
    }
    @ServerTimestamp public Date getDateCreated() { return dateCreated; }
    public String getUrlImage() { return urlImage; }

    // --- SETTERS ---
    public void setSender(User sender) {
        this.sender = sender;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }

}
