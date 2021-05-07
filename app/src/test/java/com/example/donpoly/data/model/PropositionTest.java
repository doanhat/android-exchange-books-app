package com.example.donpoly.data.model;

import junit.framework.TestCase;

import java.util.Calendar;

public class PropositionTest extends TestCase {

    public void testGetDateTimeFromCalendar() {
        Proposition prop = new Proposition();
        prop.setPostedDay(prop.getDateTimeFromCalendar(Calendar.getInstance()));
        System.out.println(prop.getPostedDay());
    }
}