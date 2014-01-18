package com.adm.meetup.event;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by jan on 18.1.14.
 */
public class EventDbContentProvider extends ContentProvider {
    private EventDatabase db = null;

    private static final String AUTHORITY = "com.adm.meetup.event.EventManager";

    public static final int EVENTS = 100;
    public static final int EVENTS_ID = 101;

    public static final String EVENTS_URI = "content://" + EventDbContentProvider.AUTHORITY + "/event";
    public static final String EVENTS_ID_URI = "content://" + EventDbContentProvider.AUTHORITY + "/event/";

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

    public EventDbContentProvider(Context context) {
        this.context = context;
        db = new EventDatabase(context);
    }

    @Override
    public boolean onCreate() {
        Log.i("acontext", this.context.toString());
        db = new EventDatabase(this.context);
        return false;
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
