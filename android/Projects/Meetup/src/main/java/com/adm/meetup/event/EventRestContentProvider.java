package com.adm.meetup.event;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Date;

/**
 * Created by jan on 18.1.14.
 */
public class EventRestContentProvider extends EventContentProvider {
    private Context context = null;
    private static final String SERVER = "http://meetup.apiary.io";

    public EventRestContentProvider(Context context) {
        this.context = context;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        int match = sURIMatcher.match(uri);

        StringBuilder baseurl= new StringBuilder(EventRestContentProvider.SERVER);
        switch(match) {
            case    1:
                baseurl.append("event");
                break;
        }

        String s = "{\"id\": 102, \"name\":\"event name\", \"date\":\"27.11.2013 13:20\", \"dueDate\":\"25.11.2013 13:20\", \"source\":\"DEFAULT\",\"types\": [{\"id\":1,\"name\": \"default\"},{\"id\":2,\"name\": \"sport\"}],\"constraints\":[{\"id\":1, \"name\": \"attendee_exact_number\"},{\"id\":2,\"name\": \"nationality_exact_number\"}],\"comments\": [{ \"id\": 1, \"user\": {\"id\":1,\"name\": \"name etc\"}, \"comment\": \"lorem ipsum...\",\"date\":\"27.11.2013 13:20\" }";
        JSONObject object;
        String[] columnNames = {"id", "name", "location", "description", "due_date", "date", "source"};
        MatrixCursor cursor = new MatrixCursor(columnNames);

        try {
            object = (JSONObject) new JSONTokener(s).nextValue();
            String[] row;
            row = new String[] {
                object.getString("id"),
                object.getString("name"),
                object.getString("location"),
                object.getString("description"),
                object.getString("due_data"),
                object.getString("data"),
                object.getString("source")
            };

            cursor.addRow(row);
        } catch (Exception e) {

        }

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)  {
       throw new UnsupportedOperationException();
    }

    private Uri getUriForId(long id, Uri uri) {
        Uri itemUri = ContentUris.withAppendedId(uri, id);
        if (id > 0) {
            this.context.
                    getContentResolver().
                    notifyChange(itemUri, null);
        }
        return itemUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }


    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
