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


public class EventManager extends ContentProvider implements IEventManager {
    private EventDatabase db = null;

    public HttpClient client = new DefaultHttpClient();
	private static final String SERVER = "http://meetup.apiary.io/";
    private static final String AUTHORITY = "com.adm.meetup.event.EventManager";

    public static final int EVENTS = 100;
    public static final int EVENTS_ID = 101;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, "event", EVENTS);
        sURIMatcher.addURI(AUTHORITY, "event/*", EVENTS_ID);
    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                return ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/com.adm.meetup.event.EventManager.Events";
            case EVENTS_ID:
                return ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/com.adm.meetup.event.EventManager.Events";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    private Context context = null;

    public EventManager(Context context) {
        this.context = context;
        db = new EventDatabase(context);
    }

    public Event getEventById(Long id) {
        Event event = null;
        Uri uri = Uri.parse("content://" + AUTHORITY + "/event/" + id.toString());
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

        Cursor result = this.query(uri, projection, selection, selectionArgs,
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

        Uri uri = Uri.parse("content://" + AUTHORITY + "/event");
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

        Cursor result = this.query(uri, projection, selection, selectionArgs, sortOrder);
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
        Uri uri = Uri.parse("content://" + AUTHORITY + "/event/" + event.getId().toString());
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
        this.update(uri, content, selection, null);
    }

    @Override
    public boolean onCreate() {
        Log.i("acontext", this.context.toString());
        db = new EventDatabase(this.context);
        return false;
    }

    public void deleteEvents() {
        Uri uri = Uri.parse("content://" + AUTHORITY + "/event/");
        String selection =  null;
        String[] selectionArgs = null;

        this.delete(uri, selection, selectionArgs);
    }

    public void createEvent(Event event) {
        Uri uri = Uri.parse("content://" + AUTHORITY + "/event/");
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
        this.insert(uri, content);
    }

    public void deleteEvent(Event event) {
        Uri uri = Uri.parse("content://" + AUTHORITY + "/event/" + event.getId().toString());
        String selection =  EventDatabase.Tables.Events.Columns.ID + "='" + event.getId().toString() +"'";
        String[] selectionArgs = null;

        this.delete(uri, selection, selectionArgs);

    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = this.db.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                builder.setTables(EventDatabase.Tables.Events.TABLE);
                break;
            case EVENTS_ID:
                builder.setTables(EventDatabase.Tables.Events.TABLE);
                builder.appendWhere(EventDatabase.Tables.Events.Columns.ID + " = " +
                        uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
        Cursor cursor =
            builder.query(
                        db,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

            cursor.setNotificationUri(
                    this.context.getContentResolver(),
                    uri);

        return cursor;
    }
    /*public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        int match = sURIMatcher.match(uri);

        StringBuilder baseurl= new StringBuilder(EventManager.SERVER);
        switch(match) {
            case    1:
                baseurl.append("event");
                break;
        }

        String s = "{\"id\": 102, \"name\":\"event name\", \"date\":\"27.11.2013 13:20\", \"dueDate\":\"25.11.2013 13:20\", \"source\":\"DEFAULT\",\"types\": [{\"id\":1,\"name\": \"default\"},{\"id\":2,\"name\": \"sport\"}],\"constraints\":[{\"id\":1, \"name\": \"attendee_exact_number\"},{\"id\":2,\"name\": \"nationality_exact_number\"}],\"comments\": [{ \"id\": 1, \"user\": {\"id\":1,\"name\": \"name etc\"}, \"comment\": \"lorem ipsum...\",\"date\":\"27.11.2013 13:20\" }";
        JSONObject object;
        String[] columnNames = {"id", "object"};
        MatrixCursor cursor = new MatrixCursor(columnNames);
        MatrixCursor.RowBuilder row = cursor.newRow();

        try {
            object = (JSONObject) new JSONTokener(s).nextValue();
            String query = object.getString("query");
            JSONArray locations = object.getJSONArray("locations");
            BaseEvent e = new Event();
            String name = "event";
            String location = "Event location";
            Long id = Long.MIN_VALUE;
            Date date = new Date(2013,12,28);
            Date dueDate = new Date(2013,12,20);

            e.setDate(date);
            e.setDueDate(dueDate);
            e.setId(id);
            e.setName(name);
            e.setLocation(location);

            row.add(id.toString(), e);
        } catch (Exception e) {

        }

        return cursor;
    }*/

    @Override
    public Uri insert(Uri uri, ContentValues values)  {
        Log.i("acontext", "acontex");
        Log.i("acontext", this.context.toString());
        SQLiteDatabase db;
        db = this.db.getWritableDatabase();
        if (sURIMatcher.match(uri) == EVENTS) {
            long id =
                    db.insert(
                            EventDatabase.Tables.Events.TABLE,
                            null,
                            values);
            return getUriForId(id, uri);
        } else {
            long id =
                    db.insertWithOnConflict(
                            EventDatabase.Tables.Events.TABLE,
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_REPLACE);
            return getUriForId(id, uri);
        }
    }

    private Uri getUriForId(long id, Uri uri) {
        Uri itemUri = ContentUris.withAppendedId(uri, id);
        if (id > 0) {
            this.context.
                getContentResolver().
                notifyChange(itemUri, null);
        }
        return itemUri;
        //throw new SQLException("Problem while inserting into uri: " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.db.getWritableDatabase();
        int delCount = 0;
        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                delCount = db.delete(
                        EventDatabase.Tables.Events.TABLE,
                        selection,
                        selectionArgs);
                break;
            case EVENTS_ID:
                delCount = db.delete(
                        EventDatabase.Tables.Events.TABLE,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        if (delCount > 0) {
            this.context.getContentResolver().notifyChange(uri, null);
        }
        return delCount;
    }


        public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            SQLiteDatabase db = this.db.getWritableDatabase();
            int updateCount = 0;
            switch (sURIMatcher.match(uri)) {
                case EVENTS:
                    updateCount = db.update(
                            EventDatabase.Tables.Events.TABLE,
                            values,
                            selection,
                            selectionArgs);
                    break;
                case EVENTS_ID:
                    Log.i("eventmanager", selection.toString());
                    updateCount = db.update(
                            EventDatabase.Tables.Events.TABLE,
                            values,
                            selection,
                            selectionArgs);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported URI: " + uri);
            }
            if (updateCount > 0) {
                this.context.getContentResolver().notifyChange(uri, null);
            }
            return updateCount;
        }

}