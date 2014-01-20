package com.adm.meetup.test;

import com.adm.meetup.event.AttendeeExactEventConstraint;
import com.adm.meetup.event.AttendeeMaxEventConstraint;
import com.adm.meetup.event.AttendeeMinEventConstraint;
import com.adm.meetup.event.BaseEvent;
import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventFieldMissingException;
import com.adm.meetup.event.EventValidationException;
import com.adm.meetup.event.FacebookEvent;
import com.adm.meetup.event.IFields;
import com.adm.meetup.event.SportEvent;

import junit.framework.TestCase;

import java.util.Date;
import java.util.Iterator;


/**
 * Created by Jan on 2.12.13.
 */
public class TestEventDecorator extends TestCase {
    private BaseEvent event = null;

    public TestEventDecorator() {
    }

    public void setUp() throws Exception {
        event = new Event();
    }

    public void tearDown() {
    }

    public void testBaseEvent() throws Exception {
        BaseEvent e = new Event();
        String name = "event";
        String location = "Event location";
        Long id = Long.MIN_VALUE;
        Date date = new Date(2013, 12, 28);
        Date dueDate = new Date(2013, 12, 20);

        e.setDate(date);
        e.setDueDate(dueDate);
        e.setId(id);
        e.setName(name);
        e.setLocation(location);

        assertEquals(e.getId(), id);
        assertEquals(e.getName(), name);
        assertEquals(e.getLocation(), location);
        assertEquals(e.getDate(), date);
        assertEquals(e.getDueDate(), dueDate);
        this.event = event;
    }

    public void testSportEventDecorator() throws Exception {
        BaseEvent event = this.event;
        Iterator<IFields> i = event.getFields();
        try {
            while (i.hasNext()) {
                IFields iField = i.next();
                event.get(iField);
            }
        } catch (EventFieldMissingException e1) {
            // OK
        }

        event = new SportEvent(event);
        try {
            while (i.hasNext()) {
                IFields iField = i.next();
                event.get(iField);
            }
        } catch (EventFieldMissingException e1) {
            fail();
        }
        try {
            String sportName = "football";
            event.set(SportEvent.Fields.SPORTNAME, sportName);
            assertEquals(event.get(SportEvent.Fields.SPORTNAME), sportName);
        } catch (EventFieldMissingException e1) {
            fail();
        }

        this.event = event;
    }

    public void testFacebookEventDecorator() throws Exception {
        BaseEvent event = this.event;

        Iterator<IFields> i = event.getFields();
        try {
            while (i.hasNext()) {
                IFields iField = i.next();
                event.get(iField);
            }

        } catch (EventFieldMissingException e1) {
            // OK
        }

        event = new SportEvent(event);
        try {
            while (i.hasNext()) {
                IFields iField = i.next();
                event.get(iField);
            }
        } catch (EventFieldMissingException e1) {
            fail();
        }

        event = new FacebookEvent(event);
        try {
            String isFacebook = "1";
            event.set(FacebookEvent.Fields.FACEBOOK, isFacebook);
            assertEquals(event.get(FacebookEvent.Fields.FACEBOOK), isFacebook);
        } catch (EventFieldMissingException e1) {
            fail();
        }

        this.event = event;
    }

    public void testAttendeeEventConstraint() throws Exception {
        BaseEvent event = this.event;
        Long attendee = 10l;
        Long minAttendee = 8l;
        Long maxAttendee = 15l;
        event.setAttendee(attendee);
        try {
            event = new AttendeeExactEventConstraint(event, attendee);
            event = new AttendeeMaxEventConstraint(event, maxAttendee);
            event = new AttendeeMinEventConstraint(event, minAttendee);
        } catch (EventValidationException e1) {
            fail();
        }

        BaseEvent event2 = new Event();
        event2.setAttendee(attendee - 1);
        try {
            event2 = new AttendeeExactEventConstraint(event2, attendee);
            fail();
        } catch (EventValidationException e1) {
            // OK
        }

        BaseEvent event3 = new Event();
        event3.setAttendee(minAttendee - 1);
        try {
            event3 = new AttendeeMinEventConstraint(event3, minAttendee);
            fail();
        } catch (EventValidationException e1) {
            // OK
        }

        BaseEvent event4 = new Event();
        event4.setAttendee(minAttendee + 1);
        try {
            event4 = new AttendeeMinEventConstraint(event4, minAttendee);

        } catch (EventValidationException e1) {
            fail();
        }

        BaseEvent event5 = new Event();
        event5.setAttendee(maxAttendee + 1);
        try {
            event5 = new AttendeeMaxEventConstraint(event5, maxAttendee);
            fail();
        } catch (EventValidationException e1) {
            // OK
        }

        BaseEvent event6 = new Event();
        event6.setAttendee(maxAttendee + 1);
        try {
            event6 = new AttendeeMinEventConstraint(event6, maxAttendee);
        } catch (EventValidationException e1) {
            fail();
        }

        this.event = event;
    }


}
