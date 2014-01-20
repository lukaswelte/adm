package com.adm.meetup.event;

import java.util.Date;

public class EventComment {
    //private User user;
    private String comment;
    private Date date;

    /*public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}