package com.adm.meetup.event;

import java.util.Date;

public class EventComment {
    private String comment;
    private Date date;
    private Long userId;
    private Long eventId;
    private Long id;

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }
}