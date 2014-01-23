package com.adm.meetup.event;

import java.util.ArrayList;

public interface IEventManager {

    public Event getEventById(Long id);

    public ArrayList<Event> getEvents();

    public void updateEvent(Event event);

    public void createEvent(Event event);

    public void deleteEvent(Event event);

    public EventComment getEventCommentById(Long id);

    public ArrayList<EventComment> getEventComments(Long eventId);

    public void createEventComment(EventComment comment);

    public void updateEventComment(EventComment comment);

    public void deleteEventComment(EventComment comment);
}