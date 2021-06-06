package com.example.donpoly.data.model;

import android.annotation.SuppressLint;

import com.example.donpoly.data.tools.JSONModel;
import com.example.donpoly.data.tools.Status;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

@IgnoreExtraProperties
public class Proposition extends JSONModel implements Comparable<Proposition>{
    private String id;
    private String title;
    private Status status;
    private String description;
    private String postedDay;
    private String validDay;
    private float price;
    private boolean exchangeable;
    private String author;
    private String taker;
    private String imageUrl;

    public Proposition(String title, Status status, String description, String postedDay, String validDay, float price, boolean exchangeable, String author, String taker, String image) {
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
        this.imageUrl=image;
    }

    public Proposition() {
        this.title = "";
        this.status = Status.ACCEPTABLE;
        this.description = "";
        this.postedDay = getDateTimeFromCalendar(Calendar.getInstance());
        this.validDay = getDateFromCalendar(Calendar.getInstance());
        this.price = 0;
        this.exchangeable = true;
        this.author = null;
        this.taker = null;
        this.id = UUID.randomUUID().toString();
        this.imageUrl=null;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTaker() {
        return taker;
    }

    public void setTaker(String taker) {
        this.taker = taker;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    //@SuppressLint("DefaultLocale")
    /*@RequiresApi(api = Build.VERSION_CODES.O)
    public String getValidDayFromLocalDate(LocalDate date){
        return String.format("%s/%s/%s",
                String.format("%02d", date.getDayOfMonth()),
                String.format("%02d", date.getMonthValue()),
                String.format("%02d", date.getYear()));
    }*/

    @SuppressLint("DefaultLocale")
    public String getDateFromCalendar(Calendar calendar){
        int mYear, mMonth, mDay;
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format("%s/%s/%s",
                String.format("%02d", mDay),
                String.format("%02d", mMonth + 1),
                String.format("%02d", mYear));
    }

    @SuppressLint("DefaultLocale")
    public String getDateTimeFromCalendar(Calendar calendar) {

        int mYear, mMonth, mDay, mHour, mMinute, mSecond;
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mSecond = calendar.get(Calendar.SECOND);
        return String.format("%s/%s/%s-%s:%s:%s",
                String.format("%02d", mDay),
                String.format("%02d", mMonth + 1),
                String.format("%02d", mYear),
                String.format("%02d", mHour),
                String.format("%02d", mMinute),
                String.format("%02d", mSecond));
    }

    @Override
    public int compareTo(Proposition p) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss", Locale.FRENCH);
        try {
            cal1.setTime(sdf.parse(this.postedDay));
            cal2.setTime(sdf.parse(p.getPostedDay()));
            return cal2.compareTo(cal1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

