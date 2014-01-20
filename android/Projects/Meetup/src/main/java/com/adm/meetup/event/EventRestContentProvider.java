package com.adm.meetup.event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.adm.meetup.helpers.NetworkHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by jan on 18.1.14.
 */
public class EventRestContentProvider extends EventContentProvider {
    private Context context = null;
    private static final String SERVERAPIPATH = "/event";
    private HttpClient client = new DefaultHttpClient();

    public EventRestContentProvider(Context context) {
        this.context = context;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        StringBuilder baseURL = new StringBuilder(EventRestContentProvider.SERVERAPIPATH);
        baseURL.append("/query");
        JsonObject selectionJsonObject = new JsonObject();
        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                break; //Nothing to do
            case EVENTS_ID:
                String eventID = uri.getLastPathSegment();
                selectionJsonObject.addProperty("id", eventID);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        //String s = "{\"id\": 102, \"name\":\"event name\", \"date\":\"27.11.2013 13:20\", \"dueDate\":\"25.11.2013 13:20\", \"source\":\"DEFAULT\",\"types\": [{\"id\":1,\"name\": \"default\"},{\"id\":2,\"name\": \"sport\"}],\"constraints\":[{\"id\":1, \"name\": \"attendee_exact_number\"},{\"id\":2,\"name\": \"nationality_exact_number\"}],\"comments\": [{ \"id\": 1, \"user\": {\"id\":1,\"name\": \"name etc\"}, \"comment\": \"lorem ipsum...\",\"date\":\"27.11.2013 13:20\" }";

        JSONObject object;
        MatrixCursor cursor = new MatrixCursor(projection);

        try {
            JsonElement receivedObject = NetworkHelper.requestBackendSynchronously(null, baseURL.toString(), selectionJsonObject, true);
            if (receivedObject.isJsonArray()) {
                Iterator<JsonElement> iterator = receivedObject.getAsJsonArray().iterator();
                while (iterator.hasNext()) {
                    JsonElement element = iterator.next();
                    object = new JSONObject(element.toString());
                    addJSONObjectToCursor(object, cursor);
                }
            } else {
                object = new JSONObject(receivedObject.toString());
                addJSONObjectToCursor(object, cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cursor;
    }

    private void addJSONObjectToCursor(JSONObject object, MatrixCursor cursor) {
        List<Object> columnValues = new ArrayList<Object>();
        String[] columnNames = cursor.getColumnNames();
        for (String columnName : columnNames) {
            Object value;
            if (columnName.equalsIgnoreCase(EventDatabase.Tables.Events.Columns.ID)) {
                try {
                    String idString = object.getString(columnName);
                    value = Long.parseLong(idString);
                } catch (JSONException e) {
                    value = "";
                }
            } else {
                try {
                    value = object.getString(columnName);
                } catch (JSONException e) {
                    value = "";
                }
            }
            columnValues.add(value);
        }

        cursor.addRow(columnValues);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        StringBuilder baseURL = new StringBuilder(EventRestContentProvider.SERVERAPIPATH);
        baseURL.append("/create");
        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                break;
            case EVENTS_ID:
                baseURL.append("/" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        JSONObject object = new JSONObject();

        try {
            object.put(EventDatabase.Tables.Events.Columns.ID, values.get(EventDatabase.Tables.Events.Columns.ID));
            object.put(EventDatabase.Tables.Events.Columns.NAME, values.get(EventDatabase.Tables.Events.Columns.NAME));
            object.put(EventDatabase.Tables.Events.Columns.LOCATION, values.get(EventDatabase.Tables.Events.Columns.LOCATION));
            object.put(EventDatabase.Tables.Events.Columns.DATE, values.get(EventDatabase.Tables.Events.Columns.DATE));
            object.put(EventDatabase.Tables.Events.Columns.DUE_DATE, values.get(EventDatabase.Tables.Events.Columns.DUE_DATE));
            object.put(EventDatabase.Tables.Events.Columns.SOURCE, values.get(EventDatabase.Tables.Events.Columns.SOURCE));
            object.put(EventDatabase.Tables.Events.Columns.DESCRIPTION, values.get(EventDatabase.Tables.Events.Columns.DESCRIPTION));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObject postedObject = new JsonParser().parse(object.toString()).getAsJsonObject();
        try {
            NetworkHelper.requestBackendSynchronously(null, baseURL.toString(), postedObject, true);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        StringBuilder baseURL = new StringBuilder(EventRestContentProvider.SERVERAPIPATH);
        baseURL.append("/destroy");
        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                break;
            case EVENTS_ID:
                baseURL.append("/" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        try {
            NetworkHelper.requestBackendSynchronously(null, baseURL.toString(), null, true);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }


    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Uri ret;
        StringBuilder baseURL = new StringBuilder(EventRestContentProvider.SERVERAPIPATH);
        baseURL.append("/update");
        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                break;
            case EVENTS_ID:
                baseURL.append("/" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        JSONObject object = new JSONObject();
        try {
            object.put(EventDatabase.Tables.Events.Columns.ID, values.get(EventDatabase.Tables.Events.Columns.ID));
            object.put(EventDatabase.Tables.Events.Columns.NAME, values.get(EventDatabase.Tables.Events.Columns.NAME));
            object.put(EventDatabase.Tables.Events.Columns.LOCATION, values.get(EventDatabase.Tables.Events.Columns.LOCATION));
            object.put(EventDatabase.Tables.Events.Columns.DATE, values.get(EventDatabase.Tables.Events.Columns.DATE));
            object.put(EventDatabase.Tables.Events.Columns.DUE_DATE, values.get(EventDatabase.Tables.Events.Columns.DUE_DATE));
            object.put(EventDatabase.Tables.Events.Columns.SOURCE, values.get(EventDatabase.Tables.Events.Columns.SOURCE));
            object.put(EventDatabase.Tables.Events.Columns.DESCRIPTION, values.get(EventDatabase.Tables.Events.Columns.DESCRIPTION));
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        JsonObject postedObject = new JsonParser().parse(object.toString()).getAsJsonObject();
        JsonElement element = null;
        try {
            element = NetworkHelper.requestBackendSynchronously(null, baseURL.toString(), postedObject, true);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (element != null) return 0;
        return 1;
    }
}
