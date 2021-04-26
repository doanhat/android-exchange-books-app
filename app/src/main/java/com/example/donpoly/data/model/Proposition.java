package com.example.donpoly.data.model;

import com.example.donpoly.data.tools.Status;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@IgnoreExtraProperties
public class Proposition {
    private String id;
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
        this.id = UUID.randomUUID().toString();
    }

    public Proposition() {
        this.title = "title";
        this.status = Status.ACCEPTABLE;
        this.description = "description";
        this.postedDay = "12/12/2021";
        this.validDay = "12/12/2021";
        this.price = 5;
        this.exchangeable = true;
        this.author = null;
        this.taker = null;
        this.id = UUID.randomUUID().toString();
    }

    public static ArrayList<Proposition> createDummies(int i) {
        ArrayList<Proposition> lProps = new ArrayList<>();
        for (int j = 0; j < i; j++) {
            lProps.add(new Proposition());
        }
        return lProps;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
