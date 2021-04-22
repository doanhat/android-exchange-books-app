package com.example.donpoly.data.model;

import com.example.donpoly.data.tools.Status;

public class Proposition {
    private String title;
    private Status status;
    private String description;
    private String postedDay;
    private String validDay;
    private float price;
    private boolean exchangeable;
    private LoggedInUser author;
    private LoggedInUser taker;

    public Proposition(String title, Status status, String description, String postedDay, String validDay, float price, boolean exchangeable, LoggedInUser author, LoggedInUser taker) {
        this.title = title;
        this.status = status;
        this.description = description;
        this.postedDay = postedDay;
        this.validDay = validDay;
        this.price = price;
        this.exchangeable = exchangeable;
        this.author = author;
        this.taker = taker;
    }

    public Proposition() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostedDay() {
        return postedDay;
    }

    public void setPostedDay(String postedDay) {
        this.postedDay = postedDay;
    }

    public String getValidDay() {
        return validDay;
    }

    public void setValidDay(String validDay) {
        this.validDay = validDay;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isExchangeable() {
        return exchangeable;
    }

    public void setExchangeable(boolean exchangeable) {
        this.exchangeable = exchangeable;
    }

    public LoggedInUser getAuthor() {
        return author;
    }

    public void setAuthor(LoggedInUser author) {
        this.author = author;
    }

    public LoggedInUser getTaker() {
        return taker;
    }

    public void setTaker(LoggedInUser taker) {
        this.taker = taker;
    }
}
