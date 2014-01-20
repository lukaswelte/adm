package com.adm.meetup.test;

import android.content.Context;
import android.os.SystemClock;
import android.test.AndroidTestCase;

import com.adm.meetup.event.Event;
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
        Event Event = new Event();
        Event.setId(Long.valueOf(1));
        Event.setName("Event name");
        Event.setLocation("location");
        Event.setDate(new Date(2013, 11, 25));
        Event.setDueDate(new Date(2013, 11, 27));

        manager.createEvent(Event);

        Long EventId = Event.getId();

        Event result = manager.getEventById(EventId);
        assertDeepEquals(Event, result);
    }


    /**
     * Test of findAllEvents method, of class EventManagerImpl.
     */
    public void testGetEvents() {
        manager.deleteEvents();
        Event Event = new Event();
        Event.setId(Long.valueOf(1));
        Event.setName("Event name");
        Event.setLocation("location");
        Event.setDate(new Date(2013, 11, 25));
        Event.setDueDate(new Date(2013, 11, 27));

        Event Event2 = new Event();
        Event2.setId((Long.valueOf(1)) + 1);
        Event2.setName("Event name 2");
        Event2.setLocation("location 2");
        Event2.setDate(new Date(2015, 11, 25));
        Event2.setDueDate(new Date(2015, 11, 27));

        manager.createEvent(Event);
        manager.createEvent(Event2);

        ArrayList<Event> expected = new ArrayList<Event>();
        expected.add(Event);
        expected.add(Event2);
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
        Event Event = new Event();
        Event.setId(Long.valueOf(1));
        Event.setName("Event name");
        Event.setLocation("location");
        Event.setDate(new Date(2013, 11, 25));
        Event.setDueDate(new Date(2013, 11, 27));

        Event Event2 = new Event();
        Event2.setId((Long.valueOf(1)) + 1);
        Event2.setName("Event name 2");
        Event2.setLocation("location 2");
        Event2.setDate(new Date(2015, 11, 25));
        Event2.setDueDate(new Date(2015, 11, 27));

        manager.createEvent(Event);
        manager.createEvent(Event2);

        Long EventId = Event.getId();

        Event = manager.getEventById(EventId);
        Event.setName("Updated event");
        manager.updateEvent(Event);
        Event = manager.getEventById(EventId);

        assertEquals("Updated event", Event.getName());
        assertEquals("location", Event.getLocation());


        Event = manager.getEventById(EventId);
        Event.setLocation("Vymyšlená ulice 300/4, 625 00 Brno");
        manager.updateEvent(Event);

        Event = manager.getEventById(EventId);
        assertEquals("Updated event", Event.getName());
        assertEquals("Vymyšlená ulice 300/4, 625 00 Brno", Event.getLocation());

        // Check if updates didn't affected other records
        assertDeepEquals(Event2, manager.getEventById(Event2.getId()));
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
        manager.createEvent(event);

        Event result = manager.getEventById(event.getId());

        assertNotSame(event, result);
        assertDeepEquals(event, result);
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

        Event Event2 = new Event();
        Event2.setId(Long.valueOf(2));
        Event2.setName("Micha Pokorný");
        Event2.setLocation("Vymyšlená ulice 300/4, 625 00 Brno");

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

    private void assertDeepEquals(ArrayList<Event> expectedList, ArrayList<Event> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Event expected = expectedList.get(i);
            Event actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

}
