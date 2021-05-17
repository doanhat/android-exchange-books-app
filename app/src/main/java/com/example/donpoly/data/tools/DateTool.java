package com.example.donpoly.data.tools;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class DateTool {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static final String getDateFromLocalDate(LocalDate date){
        return String.format("%s/%s/%s",
                String.format("%02d", date.getDayOfMonth()),
                String.format("%02d", date.getMonthValue()),
                String.format("%02d", date.getYear()));
    }
}
