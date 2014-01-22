package com.adm.meetup.event;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.adm.meetup.helpers.DateHelper;

import java.text.ParseException;
import java.util.ArrayList;


public class EventManager implements IEventManager {
    private ContentProvider contentProvider = null;

    public EventManager(Context context) {
        contentProvider = new EventRestContentProvider(context);
    }

    public EventManager(ContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    public Event getEventById(Long id) {
        Event event = null;
        Uri uri = Uri.parse(EventDbContentProvider.EVENTS_ID_URI + id.toString());
        String[] projection = new String[]{
                EventDatabase.Tables.Events.Columns.ID,
                EventDatabase.Tables.Events.Columns.DESCRIPTION,
                EventDatabase.Tables.Events.Columns.NAME,
                EventDatabase.Tables.Events.Columns.DATE,
                EventDatabase.Tables.Events.Columns.DUE_DATE,
                EventDatabase.Tables.Events.Columns.LOCATION,
        };
        String selection = EventDatabase.Tables.Events.Columns.ID + "='" + id.toString() + "'";
        String[] selectionArgs = null;
        String sortOrder = "";

        Cursor result = this.contentProvider.query(uri, projection, selection, selectionArgs, sortOrder);
        if (result != null) {
            if (result.moveToFirst()) {
                event = new Event();
                event.setId(result.getLong(result.getColumnIndex(EventDatabase.Tables.Events.Columns.ID)));
                event.setName(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.NAME)));
                event.setDescription(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.DESCRIPTION)));
                event.setLocation(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.LOCATION)));
                try {
                    event.setDate(DateHelper.parse(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.DATE))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    event.setDueDate(DateHelper.parse(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.DUE_DATE))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        result.close();
        return event;
    }

    public ArrayList<Event> getEvents() {

        Uri uri = Uri.parse(EventDbContentProvider.EVENTS_URI);
        String[] projection = new String[]{
                EventDatabase.Tables.Events.Columns.ID,
                EventDatabase.Tables.Events.Columns.DESCRIPTION,
                EventDatabase.Tables.Events.Columns.NAME,
                EventDatabase.Tables.Events.Columns.DATE,
                EventDatabase.Tables.Events.Columns.DUE_DATE,
                EventDatabase.Tables.Events.Columns.LOCATION,
        };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = "";

        Cursor result = this.contentProvider.query(uri, projection, selection, selectionArgs, sortOrder);
        ArrayList<Event> list = new ArrayList<Event>();
        if (result != null) {
            if (result.moveToFirst()) {
                do {
                    Event event = new Event();
                    event.setId(result.getLong(result.getColumnIndex(EventDatabase.Tables.Events.Columns.ID)));
                    event.setName(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.NAME)));
                    event.setDescription(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.DESCRIPTION)));
                    event.setLocation(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.LOCATION)));
                    try {
                        event.setDate(DateHelper.parse(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.DATE))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        event.setDueDate(DateHelper.parse(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.DUE_DATE))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    list.add(event);
                } while (result.moveToNext());
            }
        }
        result.close();
        return list;
    }

    public void updateEvent(Event event) {
        Uri uri = Uri.parse(EventDbContentProvider.EVENTS_ID_URI + event.getId().toString());
        String selection = EventDatabase.Tables.Events.Columns.ID + "='" + event.getId().toString() + "'";
        ContentValues content = new ContentValues();
        content.put(EventDatabase.Tables.Events.Columns.NAME, event.getName());
        content.put(EventDatabase.Tables.Events.Columns.LOCATION, event.getLocation());
        content.put(EventDatabase.Tables.Events.Columns.DATE, DateHelper.format(event.getDate()));
        content.put(EventDatabase.Tables.Events.Columns.DUE_DATE, DateHelper.format(event.getDueDate()));
        this.contentProvider.update(uri, content, selection, null);
    }

    // only for testing
    public void deleteEvents() {
        Uri uri = Uri.parse(EventDbContentProvider.EVENTS_URI);
        String selection = null;
        String[] selectionArgs = null;

        this.contentProvider.delete(uri, selection, selectionArgs);
    }

    // only for testing
    public void deleteEventComments() {
        Uri uri = Uri.parse(EventDbContentProvider.COMMENTS_URI);
        String selection = null;
        String[] selectionArgs = null;

        this.contentProvider.delete(uri, selection, selectionArgs);
    }

    public void createEvent(Event event) {
        Uri uri = Uri.parse(EventDbContentProvider.EVENTS_URI);
        ContentValues content = new ContentValues();
        content.put(EventDatabase.Tables.Events.Columns.ID, event.getId());
        content.put(EventDatabase.Tables.Events.Columns.NAME, event.getName());
        content.put(EventDatabase.Tables.Events.Columns.LOCATION, event.getLocation());
        content.put(EventDatabase.Tables.Events.Columns.DATE, DateHelper.format(event.getDate()));
        content.put(EventDatabase.Tables.Events.Columns.DUE_DATE, DateHelper.format(event.getDueDate()));
        this.contentProvider.insert(uri, content);
    }

    public void createEventComment(EventComment comment) {
        Uri uri = Uri.parse(EventDbContentProvider.COMMENTS_URI);
        ContentValues content = new ContentValues();
        content.put(EventDatabase.Tables.Comments.Columns.ID, comment.getId());
        content.put(EventDatabase.Tables.Comments.Columns.EVENT_ID, comment.getEventId());
        content.put(EventDatabase.Tables.Comments.Columns.USER_ID, comment.getUserId());
        content.put(EventDatabase.Tables.Comments.Columns.COMMENT, comment.getComment());
        content.put(EventDatabase.Tables.Comments.Columns.DATE, DateHelper.format(comment.getDate()));

        this.contentProvider.insert(uri, content);
    }

    @Override
    public void updateEventComment(EventComment comment) {
        Uri uri = Uri.parse(EventDbContentProvider.COMMENTS_ID_URI + comment.getId().toString());
        String selection = EventDatabase.Tables.Events.Columns.ID + "='" + comment.getId().toString() + "'";
        ContentValues content = new ContentValues();
        content.put(EventDatabase.Tables.Comments.Columns.COMMENT, comment.getComment());
        content.put(EventDatabase.Tables.Comments.Columns.EVENT_ID, comment.getEventId());
        content.put(EventDatabase.Tables.Comments.Columns.USER_ID, comment.getUserId());
        content.put(EventDatabase.Tables.Comments.Columns.COMMENT, comment.getComment());
        content.put(EventDatabase.Tables.Comments.Columns.DATE, DateHelper.format(comment.getDate()));

        this.contentProvider.update(uri, content, selection, null);
    }

    public void deleteEvent(Event event) {
        Uri uri = Uri.parse(EventDbContentProvider.EVENTS_ID_URI + event.getId().toString());
        String selection = EventDatabase.Tables.Events.Columns.ID + "='" + event.getId().toString() + "'";
        String[] selectionArgs = null;

        this.contentProvider.delete(uri, selection, selectionArgs);
    }

    @Override
    public EventComment getEventCommentById(Long id) {
        EventComment comment = null;
        Uri uri = Uri.parse(EventDbContentProvider.COMMENTS_ID_URI + id.toString());
        String[] projection = new String[]{
                EventDatabase.Tables.Comments.Columns.ID,
                EventDatabase.Tables.Comments.Columns.EVENT_ID,
                EventDatabase.Tables.Comments.Columns.USER_ID,
                EventDatabase.Tables.Comments.Columns.DATE,
                EventDatabase.Tables.Comments.Columns.COMMENT,
        };
        String selection = EventDatabase.Tables.Comments.Columns.ID + "='" + id.toString() + "'";
        String[] selectionArgs = null;
        String sortOrder = "";

        Cursor result = this.contentProvider.query(uri, projection, selection, selectionArgs, sortOrder);
        if (result != null) {
            if (result.moveToFirst()) {
                comment = new EventComment();
                comment.setId(result.getLong(result.getColumnIndex(EventDatabase.Tables.Events.Columns.ID)));
                comment.setUserId(result.getLong(result.getColumnIndex(EventDatabase.Tables.Comments.Columns.USER_ID)));
                comment.setEventId(result.getLong(result.getColumnIndex(EventDatabase.Tables.Comments.Columns.EVENT_ID)));
                try {
                    comment.setDate(DateHelper.parse(result.getString(result.getColumnIndex(EventDatabase.Tables.Comments.Columns.DATE))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                comment.setComment(result.getString(result.getColumnIndex(EventDatabase.Tables.Comments.Columns.COMMENT)));
            }
        }
        result.close();
        return comment;
    }

    @Override
    public ArrayList<EventComment> getEventComments(Long eventId) {
        Uri uri = Uri.parse(EventDbContentProvider.COMMENTS_URI);
        String[] projection = new String[]{
                EventDatabase.Tables.Comments.Columns.ID,
                EventDatabase.Tables.Comments.Columns.EVENT_ID,
                EventDatabase.Tables.Comments.Columns.USER_ID,
                EventDatabase.Tables.Comments.Columns.DATE,
                EventDatabase.Tables.Comments.Columns.COMMENT,
        };
        String selection = EventDatabase.Tables.Comments.Columns.ID + "='" + eventId.toString() + "'";
        String[] selectionArgs = null;
        String sortOrder = "";

        Cursor result = this.contentProvider.query(uri, projection, selection, selectionArgs, sortOrder);
        ArrayList<EventComment> list = new ArrayList<EventComment>();
        if (result != null) {
            if (result.moveToFirst()) {
                do {
                    EventComment comment = new EventComment();
                    comment.setId(result.getLong(result.getColumnIndex(EventDatabase.Tables.Comments.Columns.ID)));
                    comment.setUserId(result.getLong(result.getColumnIndex(EventDatabase.Tables.Comments.Columns.USER_ID)));
                    comment.setEventId(result.getLong(result.getColumnIndex(EventDatabase.Tables.Comments.Columns.EVENT_ID)));
                    try {
                        comment.setDate(DateHelper.parse(result.getString(result.getColumnIndex(EventDatabase.Tables.Comments.Columns.DATE))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    comment.setComment(result.getString(result.getColumnIndex(EventDatabase.Tables.Comments.Columns.COMMENT)));
                    list.add(comment);
                } while (result.moveToNext());
            }
        }
        result.close();
        return list;
    }

    public void deleteEventComment(EventComment comment) {
        Uri uri = Uri.parse(EventDbContentProvider.COMMENTS_ID_URI + comment.getId().toString());
        String selection = EventDatabase.Tables.Events.Columns.ID + "='" + comment.getId().toString() + "'";
        String[] selectionArgs = null;

        this.contentProvider.delete(uri, selection, selectionArgs);
    }
}