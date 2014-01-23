package com.adm.meetup.event;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class Event extends BaseEvent {
    public enum Fields implements IFields {
        ID("id"),
        LOCATION("location"),
        NAME("name"),
        DATE("date"),
        ATTENDEE("attendee"),
        DUEDATE("dueDate");

        private String name;

        Fields(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    private String location;
    private Long id;
    private String name;
    private Date date;
    private Date dueDate;
    private Long attendee = 0l;
    private String description;
    private Hashtable<IFields, String> fields = new Hashtable<IFields, String>();
    private Vector<EventType> eventTypeList = new Vector<EventType>();
    private Vector<EventConstraintType> eventConstraintList = new Vector<EventConstraintType>();

    public Event() {
        this.addField(Event.Fields.DUEDATE, null);
        this.addField(Event.Fields.DATE, null);
        this.addField(Event.Fields.NAME, null);
        this.addField(Event.Fields.ID, null);
        this.addField(Event.Fields.LOCATION, null);
    }

    public String get(IFields name) throws EventFieldMissingException {
        return this.getField(name);
    }

    public void set(IFields name, String value) throws EventFieldMissingException {
        if (!this.fields.containsKey(name)) {
            throw new EventFieldMissingException();
        }

        this.addField(name, value);
    }

    public Iterator<IFields> getFields() {
        return this.fields.keySet().iterator();
    }

    protected String getField(IFields name) throws EventFieldMissingException {
        if (!this.fields.containsKey(name)) {
            throw new EventFieldMissingException();
        }
        return this.fields.get(name);
    }

    protected void addField(IFields name, String value) {
        if (value == null) {
            value = "";
        }

        this.fields.put(name, value);
    }

    public Vector<EventType> getTypes() {
        return this.eventTypeList;
    }

    public void addType(EventType type) {
        this.eventTypeList.add(type);
    }

    public Long getAttendee() {
        return this.attendee;
    }

    public void setAttendee(Long attendee) {
        this.attendee = attendee;
        //this.addField(Event.Fields.ATTENDEE, this.attendee.toString());
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        // this.addField(Event.Fields.DUEDATE, this.dueDate.toString());
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
        //this.addField(Event.Fields.LOCATION, this.location);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
        //this.addField(Event.Fields.ID, this.id.toString());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        // this.addField(Event.Fields.NAME, this.name);
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
        // this.addField(Event.Fields.DATE, this.date.toString());
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