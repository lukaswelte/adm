package com.adm.meetup.event;

import java.util.Date;

public interface IEvent {
    public Long getId(); 

    public void setId(Long id);
    
    public Long getAttendee(); 

    public void setAttendee(Long attendee);

    public String getName();

    public void setName(String name);
    
    public String getLocation();

    public void setLocation(String location);

    public Date getDate();

    public void setDate(Date date);

    public Date getDueDate();

    public void setDueDate(Date dueDate);
}
