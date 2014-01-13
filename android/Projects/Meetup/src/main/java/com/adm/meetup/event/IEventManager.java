package com.adm.meetup.event;

import java.util.ArrayList;

public interface IEventManager {

    public Event getEventById(Long id);

    public ArrayList<Event> getEvents();

    public void updateEvent(Event event);

    public void createEvent(Event event);

    public void deleteEvent(Event event);
}