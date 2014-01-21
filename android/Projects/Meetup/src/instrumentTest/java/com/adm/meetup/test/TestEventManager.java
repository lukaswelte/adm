package com.adm.meetup.test;

import android.content.Context;
import android.os.SystemClock;
import android.test.AndroidTestCase;

import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventComment;
import com.adm.meetup.event.EventManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by jan on 25.11.13.
 */
abstract public class TestEventManager extends AndroidTestCase {
    protected EventManager manager;
    protected Long userId = Long.valueOf(1);

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        long endTime = SystemClock.elapsedRealtime() + TimeUnit.SECONDS.toMillis(2);

        while (null == context.getApplicationContext()) {

            if (SystemClock.elapsedRealtime() >= endTime) {
                fail();
            }

            SystemClock.sleep(16);
        }
    }

    public static void setUpClass() throws Exception {
    }

    public static void tearDownClass() throws Exception {
    }


    abstract public void setUp() throws SQLException;

    public void tearDown() {
    }

    /**
     * Test of getEventById method, of class EventManagerImpl.
     */
    public void testGetEventById() {
        manager.deleteEvents();
        Event event = new Event();
        event.setId(Long.valueOf(1));
        event.setName("Event name");
        event.setLocation("location");
        event.setDate(new Date(2013, 11, 25));
        event.setDueDate(new Date(2013, 11, 27));
        event.setDescription("description");
        manager.createEvent(event);

        Long EventId = event.getId();

        Event result = manager.getEventById(EventId);
        assertDeepEquals(event, result);
    }


    /**
     * Test of findAllEvents method, of class EventManagerImpl.
     */
    public void testGetEvents() {
        manager.deleteEvents();
        Event event = new Event();
        event.setId(Long.valueOf(1));
        event.setName("Event name");
        event.setLocation("location");
        event.setDate(new Date(2013, 11, 25));
        event.setDueDate(new Date(2013, 11, 27));
        event.setDescription("description");

        Event event2 = new Event();
        event2.setId((Long.valueOf(1)) + 1);
        event2.setName("Event name 2");
        event2.setLocation("location 2");
        event2.setDate(new Date(2015, 11, 25));
        event2.setDueDate(new Date(2015, 11, 27));
        event.setDescription("description 2");

        manager.createEvent(event);
        manager.createEvent(event2);

        ArrayList<Event> expected = new ArrayList<Event>();
        expected.add(event);
        expected.add(event2);
        ArrayList<Event> actual = manager.getEvents();

        Collections.sort(actual, idComparator);
        Collections.sort(expected, idComparator);

        assertDeepEquals(expected, actual);
    }

    /**
     * Test of updateEvent method, of class EventManagerImpl.
     */

    public void testUpdateEvent() {
        manager.deleteEvents();
        Event event = new Event();
        event.setId(Long.valueOf(1));
        event.setName("Event name");
        event.setLocation("location");
        event.setDate(new Date(2013, 11, 25));
        event.setDueDate(new Date(2013, 11, 27));
        event.setDescription("description");

        Event event2 = new Event();
        event2.setId((Long.valueOf(1)) + 1);
        event2.setName("Event name 2");
        event2.setLocation("location 2");
        event2.setDate(new Date(2015, 11, 25));
        event2.setDueDate(new Date(2015, 11, 27));
        event.setDescription("description 2");

        manager.createEvent(event);
        manager.createEvent(event2);

        Long eventId = event.getId();

        event = manager.getEventById(eventId);
        event.setName("Updated event");
        manager.updateEvent(event);
        event = manager.getEventById(eventId);

        assertEquals("Updated event", event.getName());
        assertEquals("location",event.getLocation());


        event = manager.getEventById(eventId);
        event.setLocation("Vymyšlená ulice 300/4, 625 00 Brno");
        manager.updateEvent(event);

        event = manager.getEventById(eventId);
        assertEquals("Updated event", event.getName());
        assertEquals("Vymyšlená ulice 300/4, 625 00 Brno", event.getLocation());

        // Check if updates didn't affected other records
        assertDeepEquals(event2, manager.getEventById(event2.getId()));
    }

    public void updateEventWithWrongAttributes() {
        manager.deleteEvents();
        Event Event = new Event();
        Event.setId(Long.valueOf(1));
        Event.setName("Jan Jílek");
        Event.setLocation("Fleischnerova 930/7, 635 00 Brno");

        manager.createEvent(Event);
        Long EventId = Event.getId();

        try {
            manager.updateEvent(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            Event = manager.getEventById(EventId);
            Event.setId(null);
            manager.updateEvent(Event);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            Event = manager.getEventById(EventId);
            Event.setId(EventId - 1);
            manager.updateEvent(Event);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }

    public void deleteEventWithWrongAttributes() {
        manager.deleteEvents();
        Event Event = new Event();
        Event.setId(Long.valueOf(1));
        Event.setName("Jan Jílek");
        Event.setLocation("Fleischnerova, 635 00 Brno");

        try {
            manager.deleteEvent(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            Event.setId(null);
            manager.deleteEvent(Event);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            Event.setId(1l);
            manager.deleteEvent(Event);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

    }

    /**
     * Test of createEvent method, of class EventManagerImpl.
     */

    public void testCreateEvent() {
        manager.deleteEvents();
        Event event = new Event();
        event.setId(Long.valueOf(1));
        event.setName("event name");
        event.setLocation("location");
        event.setDate(new Date(12, 12, 2014));
        event.setDueDate(new Date(12, 10, 2014));
        event.setDescription("description");

        manager.createEvent(event);

        Event result = manager.getEventById(event.getId());

        assertNotSame(event, result);
        assertDeepEquals(event, result);
    }

    /**
     * Test of createEventComment method, of class EventManagerImpl.
     */

    public void testCreateEventComment() {
        manager.deleteEvents();
        Event event = new Event();
        event.setId(Long.valueOf(1));
        event.setName("event name");
        event.setLocation("location");
        event.setDate(new Date(12, 12, 2014));
        event.setDueDate(new Date(12, 10, 2014));
        event.setDescription("description");
        manager.createEvent(event);

        EventComment comment = new EventComment();
        comment.setId(Long.valueOf(1));
        comment.setEventId(event.getId());
        comment.setUserId(userId);
        comment.setDate(new Date(12,12,2014));
        comment.setComment("comment");

        manager.createEventComment(comment);
        EventComment result = manager.getEventCommentById(comment.getId());

        assertNotSame(comment, result);
        assertDeepCommentEquals(comment, result);
    }

    private static Comparator<Event> idComparator = new Comparator<Event>() {

        @Override
        public int compare(Event c1, Event c2) {
            return Long.valueOf(c1.getId()).compareTo(Long.valueOf(c2.getId()));
        }
    };

    /**
     * Test of deleteEvent method, of class EventManagerImpl.
     */
    public void testDeleteEvent() {
        manager.deleteEvents();
        Event Event = new Event();
        Event.setId(Long.valueOf(1));
        Event.setName("Jan Jílek");
        Event.setLocation("Fleischnerova, 635 00 Brno");
        Event.setDate(new Date(12, 12, 2014));
        Event.setDueDate(new Date(12, 10, 2014));
        Event.setDescription("description");

        Event Event2 = new Event();
        Event2.setId(Long.valueOf(2));
        Event2.setName("Micha Pokorný");
        Event2.setLocation("Vymyšlená ulice 300/4, 625 00 Brno");
        Event2.setDate(new Date(12, 12, 2014));
        Event2.setDueDate(new Date(12, 10, 2014));
        Event2.setDescription("description");

        manager.createEvent(Event);
        manager.createEvent(Event2);

        assertNotNull(manager.getEventById(Event.getId()));
        assertNotNull(manager.getEventById(Event2.getId()));

        manager.deleteEvent(Event);

        assertNull(manager.getEventById(Event.getId()));
        assertNotNull(manager.getEventById(Event2.getId()));
    }

    private void assertDeepEquals(Event expected, Event actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLocation(), actual.getLocation());
    }

    private void assertDeepCommentEquals(EventComment expected, EventComment actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getEventId(), actual.getEventId());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getComment(), actual.getComment());

    }

    private void assertDeepCommentEquals(ArrayList<EventComment> expectedList, ArrayList<EventComment> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            EventComment expected = expectedList.get(i);
            EventComment actual = actualList.get(i);
            assertDeepCommentEquals(expected, actual);
        }
    }

    private void assertDeepEquals(ArrayList<Event> expectedList, ArrayList<Event> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Event expected = expectedList.get(i);
            Event actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

}
