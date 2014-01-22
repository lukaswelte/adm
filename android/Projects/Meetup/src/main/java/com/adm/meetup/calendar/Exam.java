package com.adm.meetup.calendar;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lukas on 22.01.14.
 */
public class Exam implements Parcelable {

    public static final String DATE_FORMAT = "dd.MM.yyyy HH:mm";
    private String id;
    private Date date;
    private Date notifyDate;
    private String name;

    public Exam(String id, Date date, Date notifyDate, String name) {
        this.id = id;
        this.date = date;
        this.notifyDate = notifyDate;
        this.name = name;
    }

    public Exam(String id, String dateString, String notifyDateString, String name) {
        this.id = id;
        this.date = dateFromString(dateString);
        this.notifyDate = dateFromString(notifyDateString);
        this.name = name;
    }

    public Exam(JsonObject jsonObject) {
        this.id = jsonObject.get("id").getAsString();
        String dateString = jsonObject.get("date").getAsString();
        this.date = dateFromString(dateString);
        try {
            String notifyDateString = jsonObject.get("notifydate").getAsString();
            this.notifyDate = dateFromString(notifyDateString);
        } catch (Exception e) {
            this.notifyDate = null;
        }
        this.name = jsonObject.get("name").getAsString();
    }

    public JsonObject asJsonObject() {
        JsonObject postedJsonObject = new JsonObject();
        postedJsonObject.addProperty("id", this.id);
        postedJsonObject.addProperty("date", stringFromDate(this.date));
        postedJsonObject.addProperty("notifydate", stringFromDate(this.notifyDate));
        postedJsonObject.addProperty("name", this.name);

        return postedJsonObject;
    }

    public Date getDate() {
        return date;
    }

    public Date getNotifyDate() {
        return notifyDate;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }


    public static String getDateYearIdentifierOfDate(Date date) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);

        return "year:" + cal1.get(Calendar.YEAR) + "date:" + cal1.get(Calendar.DAY_OF_YEAR);
    }

    public String getDateYearIdentifier() {
        return Exam.getDateYearIdentifierOfDate(date);
    }

    private static Date dateFromString(String dateString) {
        if (dateString == null) return null;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date result;
        try {
            result = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }

    private static String stringFromDate(Date date) {
        if (date == null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeString(stringFromDate(this.date));
        parcel.writeString(stringFromDate(this.notifyDate));

    }


    private Exam(Parcel parcel) {
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.date = dateFromString(parcel.readString());
        this.notifyDate = dateFromString(parcel.readString());
    }

    public static final Creator<Exam> CREATOR = new Creator<Exam>() {
        public Exam createFromParcel(Parcel in) {
            return new Exam(in);
        }

        public Exam[] newArray(int size) {
            return new Exam[size];
        }
    };
}
