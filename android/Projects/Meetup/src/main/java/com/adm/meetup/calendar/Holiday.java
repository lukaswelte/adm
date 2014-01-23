package com.adm.meetup.calendar;

import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lukas on 21.01.14.
 */
public class Holiday {

    private String name;
    private Date date;

    public Holiday(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public Holiday(JsonObject element) {
        this.name = element.get("englishName").getAsString();

        JsonObject date = element.get("date").getAsJsonObject();
        this.date = new Date(date.get("year").getAsInt() - 1900, date.get("month").getAsInt() - 1, date.get("day").getAsInt());
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public boolean isOnTheSameDay(Date date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date);
        cal2.setTime(this.getDate());

        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }
}
