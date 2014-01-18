package com.adm.meetup.event;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;


public class EventManager implements IEventManager {
    private ContentProvider contentProvider = null;

    public EventManager(Context context) {
        contentProvider = new EventDbContentProvider(context);
    }

    public EventManager(Context context, ContentProvider contentProvider) {
        contentProvider = new EventDbContentProvider(context);
        this.contentProvider = contentProvider;
    }

    public Event getEventById(Long id) {
        Event event = null;
        Uri uri = Uri.parse(EventDbContentProvider.EVENTS_ID_URI + id.toString());
        String[] projection = new String[] {
                EventDatabase.Tables.Events.Columns.ID,
                EventDatabase.Tables.Events.Columns.DESCRIPTION,
                EventDatabase.Tables.Events.Columns.NAME,
                EventDatabase.Tables.Events.Columns.DATE,
                EventDatabase.Tables.Events.Columns.DUE_DATE,
                EventDatabase.Tables.Events.Columns.LOCATION,
        };
        String selection =  EventDatabase.Tables.Events.Columns.ID + "='" + id.toString() +"'";
        String[] selectionArgs = null;
        String sortOrder = "";

        Cursor result = this.contentProvider.query(uri, projection, selection, selectionArgs,
                sortOrder);
        if (result != null ) {
            if  (result.moveToFirst()) {
                event = new Event();
                event.setId(result.getLong(result.getColumnIndex(EventDatabase.Tables.Events.Columns.ID)));
                event.setName(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.NAME)));
                //event.setDescription(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.DESCRIPTION)));
                event.setLocation(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.LOCATION)));
            }
        }
        result.close();
        return event;
    }

    public ArrayList<Event> getEvents() {

        Uri uri = Uri.parse(EventDbContentProvider.EVENTS_URI);
        String[] projection = new String[] {
                EventDatabase.Tables.Events.Columns.ID,
                EventDatabase.Tables.Events.Columns.DESCRIPTION,
                EventDatabase.Tables.Events.Columns.NAME,
                EventDatabase.Tables.Events.Columns.DATE,
                EventDatabase.Tables.Events.Columns.DUE_DATE,
                EventDatabase.Tables.Events.Columns.LOCATION,
        };
        String selection =  null;
        String[] selectionArgs = null;
        String sortOrder = "";

        Cursor result = this.contentProvider.query(uri, projection, selection, selectionArgs, sortOrder);
        ArrayList<Event> list = new ArrayList<Event>();
        if (result != null ) {
            if  (result.moveToFirst()) {
                do {
                    Event event = new Event();
                    event.setId(result.getLong(result.getColumnIndex(EventDatabase.Tables.Events.Columns.ID)));
                    event.setName(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.NAME)));
                    event.setLocation(result.getString(result.getColumnIndex(EventDatabase.Tables.Events.Columns.LOCATION)));
                    list.add(event);
                } while(result.moveToNext());
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
        try {
            content.put(EventDatabase.Tables.Events.Columns.DATE, event.getDate().toString());
        } catch (NullPointerException e) {}
        try {
            content.put(EventDatabase.Tables.Events.Columns.DUE_DATE, event.getDueDate().toString());
        } catch (NullPointerException e) {}
        this.contentProvider.update(uri, content, selection, null);
    }

    public void deleteEvents() {
        Uri uri = Uri.parse(EventDbContentProvider.EVENTS_URI);
        String selection =  null;
        String[] selectionArgs = null;

        this.contentProvider.delete(uri, selection, selectionArgs);
    }

    public void createEvent(Event event) {
        Uri uri = Uri.parse(EventDbContentProvider.EVENTS_URI);
        ContentValues content = new ContentValues();
        content.put(EventDatabase.Tables.Events.Columns.ID, event.getId());
        content.put(EventDatabase.Tables.Events.Columns.NAME, event.getName());
        content.put(EventDatabase.Tables.Events.Columns.LOCATION, event.getLocation());
        try {
            content.put(EventDatabase.Tables.Events.Columns.DATE, event.getDate().toString());
        } catch (NullPointerException e) {}
        try {
            content.put(EventDatabase.Tables.Events.Columns.DUE_DATE, event.getDueDate().toString());
        } catch (NullPointerException e) {}
        this.contentProvider.insert(uri, content);
    }

    public void deleteEvent(Event event) {
        Uri uri = Uri.parse(EventDbContentProvider.EVENTS_ID_URI + event.getId().toString());
        String selection =  EventDatabase.Tables.Events.Columns.ID + "='" + event.getId().toString() +"'";
        String[] selectionArgs = null;

        this.contentProvider.delete(uri, selection, selectionArgs);
    }
}