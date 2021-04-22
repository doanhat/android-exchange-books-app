package com.example.donpoly.data.model;

public class Message {
    private LoggedInUser sender;
    private LoggedInUser receiver;
    private String content;
    private String time;

    public Message(LoggedInUser sender, LoggedInUser receiver, String content, String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.time = time;
    }

    public Message() {
    }

    public LoggedInUser getSender() {
        return sender;
    }

    public void setSender(LoggedInUser sender) {
        this.sender = sender;
    }

    public LoggedInUser getReceiver() {
        return receiver;
    }

    public void setReceiver(LoggedInUser receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
