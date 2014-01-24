package com.adm.meetup.test;

import android.content.Context;
import android.os.SystemClock;
import android.test.AndroidTestCase;
import android.util.Log;

import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventComment;
import com.adm.meetup.event.EventDatabase;
import com.adm.meetup.event.EventManager;
import com.adm.meetup.event.EventType;

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
    protected EventDatabase db;

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
     * Test of getEventCommentById method, of class EventManagerImpl.
     */
    public void testGetEventCommentById() {
        manager.deleteEvents();
        manager.deleteEventComments();

        EventComment comment = new EventComment();
        comment.setId(Long.valueOf(1));
        comment.setEventId(Long.valueOf(1));
        comment.setUserId(userId);
        comment.setDate(new Date(2014, 12, 12));
        comment.setComment("comment");

        manager.createEventComment(comment);
        EventComment result = manager.getEventCommentById(comment.getId());

        assertDeepCommentEquals(comment, result);
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
        event2.setId((Long.valueOf(2)));
        event2.setName("Event name 2");
        event2.setLocation("location 2");
        event2.setDate(new Date(2015, 11, 25));
        event2.setDueDate(new Date(2015, 11, 27));
        event2.setDescription("description 2");

        manager.createEvent(event);
        manager.createEvent(event2);

        Long eventId = event.getId();

        // test setName
        event = manager.getEventById(eventId);
        event.setName("Updated ic_event_sidebar");
        manager.updateEvent(event);
        Event result = manager.getEventById(eventId);
        assertDeepEquals(event, result);

        // test setLocation
        event = manager.getEventById(eventId);
        event.setLocation("Vymyšlená ulice 300/4, 625 00 Brno");
        manager.updateEvent(event);
        result = manager.getEventById(eventId);
        assertDeepEquals(event, result);

        // test setDate
        event = manager.getEventById(eventId);
        event.setDate(new Date(2010, 5, 5));
        manager.updateEvent(event);
        result = manager.getEventById(eventId);
        assertDeepEquals(event, result);

        // test setDueDate
        event = manager.getEventById(eventId);
        event.setDueDate(new Date(2002, 5, 5));
        manager.updateEvent(event);
        result = manager.getEventById(eventId);
        assertDeepEquals(event, result);

        // test setDescription
        event = manager.getEventById(eventId);
        event.setDescription("new description");
        manager.updateEvent(event);
        result = manager.getEventById(eventId);
        assertDeepEquals(event, result);

        // Check if updates didn't affected other records
        assertDeepEquals(event2, manager.getEventById(event2.getId()));
    }

    /**
     * Test of createEventComment method, of class EventManagerImpl.
     */

    public void testCreateEventComment() {
        manager.deleteEvents();
        manager.deleteEventComments();
        Event event = new Event();
        event.setId(Long.valueOf(1));
        event.setName("ic_event_sidebar name");
        event.setLocation("location");
        event.setDate(new Date(12, 12, 2014));
        event.setDueDate(new Date(12, 10, 2014));
        event.setDescription("description");
        manager.createEvent(event);

        EventComment comment = new EventComment();
        comment.setId(Long.valueOf(1));
        comment.setEventId(event.getId());
        comment.setUserId(userId);
        comment.setDate(new Date(12, 12, 2014));
        comment.setComment("comment");

        manager.createEventComment(comment);
        EventComment result = manager.getEventCommentById(comment.getId());

        Log.i("eventcommenttest", result.getUserId().toString());
        Log.i("eventcommenttest", comment.getUserId().toString());

        assertNotSame(comment, result);
        assertDeepCommentEquals(comment, result);
    }

    /**
     * Test of createEvent method, of class EventManagerImpl.
     */

    public void testCreateEvent() {
        manager.deleteEvents();
        Event event = new Event();
        event.setId(Long.valueOf(1));
        event.setName("ic_event_sidebar name");
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
     * Test of createEvent method, of class EventManagerImpl.
     */

    public void testCreateEventWithType() {
        manager.deleteEvents();
        Event event = new Event();
        event.setId(Long.valueOf(1));
        event.setName("ic_event_sidebar name");
        event.setLocation("location");
        event.setDate(new Date(12, 12, 2014));
        event.setDueDate(new Date(12, 10, 2014));
        event.setDescription("description");
        event.addType(EventType.SPORT);
        event.addType(EventType.DEFAULT);

        manager.createEvent(event);

        Event result = manager.getEventById(event.getId());

        assertEquals(event.getTypes().size(), result.getTypes().size());
        assertNotSame(event, result);
        assertDeepEquals(event, result);
    }

    /**
     * Test of updateEvent method, of class EventManagerImpl.
     */

    public void testUpdateEventComment() {
        manager.deleteEvents();
        manager.deleteEventComments();
        Event event = new Event();
        event.setId(Long.valueOf(1));
        event.setName("Event name");
        event.setLocation("location");
        event.setDate(new Date(2013, 11, 25));
        event.setDueDate(new Date(2013, 11, 27));
        event.setDescription("description");

        manager.createEvent(event);

        EventComment comment = new EventComment();
        comment.setId(Long.valueOf(1));
        comment.setEventId(event.getId());
        comment.setUserId(userId);
        comment.setDate(new Date(2013, 12, 12));
        comment.setComment("comment");

        manager.createEventComment(comment);

        EventComment comment2 = new EventComment();
        comment2.setId(Long.valueOf(2));
        comment2.setEventId(event.getId());
        comment2.setUserId(userId);
        comment2.setDate(new Date(2014, 12, 12));
        comment2.setComment("comment 2");

        manager.createEventComment(comment2);

        Long commentId = comment.getId();

        comment = manager.getEventCommentById(commentId);
        comment.setEventId(Long.valueOf(2));
        manager.updateEventComment(comment);
        EventComment result = manager.getEventCommentById(commentId);

        assertDeepCommentEquals(comment, result);

        // test setUserId
        comment = manager.getEventCommentById(commentId);
        comment.setUserId(3l);
        manager.updateEventComment(comment);
        result = manager.getEventCommentById(commentId);
        assertDeepCommentEquals(comment, result);

        // test setDate
        comment = manager.getEventCommentById(commentId);
        comment.setDate(new Date(2012, 10, 10));
        manager.updateEventComment(comment);
        result = manager.getEventCommentById(commentId);
        assertDeepCommentEquals(comment, result);

        // test setComment
        comment = manager.getEventCommentById(commentId);
        comment.setComment("comment update");
        manager.updateEventComment(comment);
        result = manager.getEventCommentById(commentId);
        assertDeepCommentEquals(comment, result);


        // Check if updates didn't affected other records
        assertDeepCommentEquals(comment2, manager.getEventCommentById(comment2.getId()));


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


    private static Comparator<Event> idComparator = new Comparator<Event>() {

        @Override
        public int compare(Event c1, Event c2) {
            return Long.valueOf(c1.getId()).compareTo(Long.valueOf(c2.getId()));
        }
    };

    /**
     * Test of deleteEventComment method, of class EventManagerImpl.
     */
    public void testDeleteEventComment() {
        manager.deleteEvents();
        Event event = new Event();
        event.setId(Long.valueOf(1));
        event.setName("Jan Jílek");
        event.setLocation("Fleischnerova, 635 00 Brno");
        event.setDate(new Date(12, 12, 2014));
        event.setDueDate(new Date(12, 10, 2014));
        event.setDescription("description");
        manager.createEvent(event);

        EventComment comment = new EventComment();
        comment.setId(Long.valueOf(1));
        comment.setEventId(event.getId());
        comment.setUserId(userId);
        comment.setDate(new Date(12, 12, 2014));
        comment.setComment("comment");

        manager.createEventComment(comment);

        EventComment comment2 = new EventComment();
        comment2.setId(Long.valueOf(2));
        comment2.setEventId(event.getId());
        comment2.setUserId(userId);
        comment2.setDate(new Date(12, 12, 2014));
        comment2.setComment("comment");

        manager.createEventComment(comment2);

        manager.deleteEventComment(comment);

        assertNull(manager.getEventCommentById(comment.getId()));
        assertNotNull(manager.getEventCommentById(comment2.getId()));
    }

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
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getDueDate(), actual.getDueDate());
        assertEquals(expected.getDescription(), actual.getDescription());
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
