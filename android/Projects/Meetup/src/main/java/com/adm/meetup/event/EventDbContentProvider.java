package com.adm.meetup.event;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by jan on 18.1.14.
 */
public class EventDbContentProvider extends EventContentProvider {
    private EventDatabase db = null;
    private Context context = null;

    public EventDbContentProvider(Context context) {
        this.context = context;
        db = new EventDatabase(context);
    }

    @Override
    public boolean onCreate() {
        db = new EventDatabase(this.context);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = this.db.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                builder.setTables(EventDatabase.Tables.Events.TABLE);
                break;
            case EVENTS_ID:
                builder.setTables(EventDatabase.Tables.Events.TABLE);
                builder.appendWhere(EventDatabase.Tables.Events.Columns.ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(this.context.getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)  {
        SQLiteDatabase db;
        db = this.db.getWritableDatabase();
        Uri ret;
        Long id;
        switch(sURIMatcher.match(uri)) {
            case EVENTS:
                id = db.insert(EventDatabase.Tables.Events.TABLE, null, values);
                ret = getUriForId(id, uri);
             break;
            case EVENTS_ID:
                id = db.insertWithOnConflict(EventDatabase.Tables.Events.TABLE, null, values,SQLiteDatabase.CONFLICT_REPLACE);
                ret = getUriForId(id, uri);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return ret;
    }

    private Uri getUriForId(long id, Uri uri) {
        Uri itemUri = ContentUris.withAppendedId(uri, id);
        if (id > 0) {
            this.context.getContentResolver().notifyChange(itemUri, null);
        }
        return itemUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.db.getWritableDatabase();
        int delCount = 0;
        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                delCount = db.delete(EventDatabase.Tables.Events.TABLE, selection, selectionArgs);
                break;
            case EVENTS_ID:
                delCount = db.delete(EventDatabase.Tables.Events.TABLE, selection, selectionArgs);
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
                updateCount = db.update(EventDatabase.Tables.Events.TABLE, values, selection, selectionArgs);
                break;
            case EVENTS_ID:
                updateCount = db.update(EventDatabase.Tables.Events.TABLE, values,selection, selectionArgs);
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
