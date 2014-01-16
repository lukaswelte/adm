package com.adm.meetup.test;

import android.content.ContentProvider;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ProviderTestCase2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.adm.meetup.EventActivity;
import com.adm.meetup.MainActivity;
import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventManager;

/**
 * Created by jan on 25.11.13.
 */
public class TestEventManager extends ActivityInstrumentationTestCase2<EventActivity> {
    private EventManager manager;

    public TestEventManager() {
        super(EventActivity.class);
    }

    public static void setUpClass() throws Exception {
    }

    public static void tearDownClass() throws Exception {
    }


    public void setUp() throws SQLException {
        manager = new EventManager();
    }

    public void tearDown() {
    }

    /**
     * Test of getEventById method, of class EventManagerImpl.
     */
    public void testGetEventById() {
        assertNull(manager.getEventById(1l));

        Event Event = new Event();
        Event.setId(Long.MIN_VALUE);
        Event.setName("Event name");
        Event.setLocation("location");
        Event.setDate(new Date(2013,11,25));
        Event.setDueDate(new Date(2013,11,27));

        manager.createEvent(Event);

        Long EventId = Event.getId();

        Event result = manager.getEventById(EventId);
        assertEquals(Event, result);
        assertDeepEquals(Event, result);
    }


    /**
     * Test of findAllEvents method, of class EventManagerImpl.
     */
    public void testGetEvents() {
        assertTrue(manager.getEvents().isEmpty());

        Event Event = new Event();
        Event.setId(Long.MIN_VALUE);
        Event.setName("Event name");
        Event.setLocation("location");
        Event.setDate(new Date(2013, 11, 25));
        Event.setDueDate(new Date(2013, 11, 27));

        Event Event2 = new Event();
        Event2.setId((Long.MIN_VALUE)+1);
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
        Collections.sort(expected,idComparator);

        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }

    /**
     * Test of updateEvent method, of class EventManagerImpl.
     */

    public void testUpdateEvent() {
        Event Event = new Event();
        Event.setId(Long.MIN_VALUE);
        Event.setName("Event name");
        Event.setLocation("location");
        Event.setDate(new Date(2013, 11, 25));
        Event.setDueDate(new Date(2013, 11, 27));

        Event Event2 = new Event();
        Event2.setId((Long.MIN_VALUE)+1);
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
        assertEquals("Updated event", Event.getName());
        assertEquals("location", Event.getLocation());


        Event = manager.getEventById(EventId);
        Event.setLocation("Vymyšlená ulice 300/4, 625 00 Brno");
        manager.updateEvent(Event);
        assertEquals("Updated event", Event.getName());
        assertEquals("Vymyšlená ulice 300/4, 625 00 Brno", Event.getLocation());

        // Check if updates didn't affected other records
        assertDeepEquals(Event2, manager.getEventById(Event2.getId()));
    }

    public void updateEventWithWrongAttributes() {

        Event Event = new Event();
        Event.setId(Long.MIN_VALUE);
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

        Event Event = new Event();
        Event.setId(Long.MIN_VALUE);
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
        Event Event = new Event();
        Event.setId(Long.MIN_VALUE);
        Event.setName("Jan Jílek");
        Event.setLocation("Fleischnerova, 635 00 Brno");

        manager.createEvent(Event);

        Event result = manager.getEventById(Event.getId());
        assertEquals(Event, result);
        assertNotSame(Event, result);
        assertDeepEquals(Event, result);
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
        Event Event = new Event();
        Event.setId(Long.MIN_VALUE);
        Event.setName("Jan Jílek");
        Event.setLocation("Fleischnerova, 635 00 Brno");

        Event Event2 = new Event();
        Event2.setId((Long.MIN_VALUE)+1);
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