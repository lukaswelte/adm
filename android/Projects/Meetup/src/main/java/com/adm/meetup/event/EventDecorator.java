package com.adm.meetup.event;

import java.util.Date;
import java.util.Iterator;

public abstract class EventDecorator extends BaseEvent {
    private BaseEvent event = null;

    public EventDecorator(BaseEvent event) {
        this.event = event;
    }

    public Long getAttendee() {
        return this.event.getAttendee();
    }

    public void setAttendee(Long attendee) {
        this.event.setAttendee(attendee);
    }

    protected String getField(IFields name) throws EventFieldMissingException {
        return event.getField(name);
    }

    protected void addField(IFields name, String value) {
        event.addField(name, value);
    }

    public String get(IFields name) throws EventFieldMissingException {
        return this.event.get(name);
    }

    public void set(IFields name, String value) throws EventFieldMissingException {
        this.event.set(name, value);
    }

    public Iterator<IFields> getFields() {
        return this.event.getFields();
    }

    public Date getDueDate() {
        return this.event.getDueDate();
    }

    public void setDueDate(Date dueDate) {
        this.event.setDueDate(dueDate);
    }

    public String getLocation() {
        return this.event.getLocation();
    }

    public void setLocation(String location) {
        this.event.setLocation(location);
    }

    public Long getId() {
        return this.event.getId();
    }

    public void setId(Long id) {
        this.event.setId(id);
    }

    public String getDescription() {
        return this.event.getDescription();
    }

    public void setDescription(String description) {
        this.event.setDescription(description);
    }

    public String getName() {
        return this.event.getName();
    }

    public void setName(String name) {
        this.event.setName(name);
    }

    public Date getDate() {
        return this.event.getDate();
    }

    public void setDate(Date date) {
        this.event.setDate(date);
    }
}