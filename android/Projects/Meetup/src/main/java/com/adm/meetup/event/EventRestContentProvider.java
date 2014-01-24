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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by jan on 18.1.14.
 */
public class EventRestContentProvider extends EventContentProvider {
    private static final String SERVERAPIPATH = "";
    private HttpClient client = new DefaultHttpClient();

    public EventRestContentProvider(Context context) {
        Context context1 = context;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        StringBuilder baseURL = new StringBuilder(EventRestContentProvider.SERVERAPIPATH);

        JsonObject selectionJsonObject = new JsonObject();
        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                baseURL.append("/event");
                break; //Nothing to do
            case EVENTS_ID:
                baseURL.append("/event");
                String eventID = uri.getLastPathSegment();
                selectionJsonObject.addProperty("id", eventID);
                break;
            case COMMENTS:
                baseURL.append("/comment");
                break; //Nothing to do
            case COMMENTS_ID:
                baseURL.append("/comment");
                eventID = uri.getLastPathSegment();
                selectionJsonObject.addProperty("id", eventID);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        baseURL.append("/query");

        //String s = "{\"id\": 102, \"name\":\"ic_event_sidebar name\", \"date\":\"27.11.2013 13:20\", \"dueDate\":\"25.11.2013 13:20\", \"source\":\"DEFAULT\",\"types\": [{\"id\":1,\"name\": \"default\"},{\"id\":2,\"name\": \"sport\"}],\"constraints\":[{\"id\":1, \"name\": \"attendee_exact_number\"},{\"id\":2,\"name\": \"nationality_exact_number\"}],\"comments\": [{ \"id\": 1, \"user\": {\"id\":1,\"name\": \"name etc\"}, \"comment\": \"lorem ipsum...\",\"date\":\"27.11.2013 13:20\" }";

        JSONObject object;
        MatrixCursor cursor = new MatrixCursor(projection);

        try {
            JsonElement receivedObject = NetworkHelper.requestBackendSynchronously(null, baseURL.toString(), selectionJsonObject, true);
            if (receivedObject.isJsonArray()) {
                for (JsonElement element : receivedObject.getAsJsonArray()) {
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
            if (columnName.equalsIgnoreCase(EventDatabase.Tables.Events.Columns.ID) || columnName.equalsIgnoreCase(EventDatabase.Tables.Comments.Columns.ID) ) {
                try {
                    String idString = object.getString(columnName);
                    value = Long.parseLong(idString);
                } catch (JSONException e) {
                    value = "";
                }
            }  else {
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

        JSONObject object = new JSONObject();
        StringBuilder baseURL = new StringBuilder(EventRestContentProvider.SERVERAPIPATH);

        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                baseURL.append("/event");
                break;
            case EVENTS_ID:
                baseURL.append("/event");
                baseURL.append("/").append(uri.getLastPathSegment());
                object = this.toEvent(values);
                break;
            case COMMENTS:
                baseURL.append("/comment");
                break;
            case COMMENTS_ID:
                baseURL.append("/comment");
                object = this.toComment(values);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        baseURL.append("/create");


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

    private JSONObject toEvent(ContentValues values) {
        JSONObject object = new JSONObject();
        try {
            object.put(EventDatabase.Tables.Events.Columns.ID, values.get(EventDatabase.Tables.Events.Columns.ID));
            object.put(EventDatabase.Tables.Events.Columns.NAME, values.get(EventDatabase.Tables.Events.Columns.NAME));
            object.put(EventDatabase.Tables.Events.Columns.LOCATION, values.get(EventDatabase.Tables.Events.Columns.LOCATION));
            object.put(EventDatabase.Tables.Events.Columns.DATE, values.get(EventDatabase.Tables.Events.Columns.DATE));
            object.put(EventDatabase.Tables.Events.Columns.DUE_DATE, values.get(EventDatabase.Tables.Events.Columns.DUE_DATE));
            object.put(EventDatabase.Tables.Events.Columns.SOURCE, values.get(EventDatabase.Tables.Events.Columns.SOURCE));
            object.put(EventDatabase.Tables.Events.Columns.DESCRIPTION, values.get(EventDatabase.Tables.Events.Columns.DESCRIPTION));
            byte[] types = values.getAsByteArray(EventDatabase.Tables.Events.Columns.TYPE);
            String type = Arrays.toString(types);
            object.put(EventDatabase.Tables.Events.Columns.TYPE, type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private JSONObject toComment(ContentValues values) {
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
        return object;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        StringBuilder baseURL = new StringBuilder(EventRestContentProvider.SERVERAPIPATH);

        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                baseURL.append("/event");
                break;
            case EVENTS_ID:
                baseURL.append("/event");
                baseURL.append("/").append(uri.getLastPathSegment());
                break;
            case COMMENTS:
                baseURL.append("/comment");
                break;
            case COMMENTS_ID:
                baseURL.append("/comment");
                baseURL.append("/").append(uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        baseURL.append("/destroy");

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
        JSONObject object = new JSONObject();
        StringBuilder baseURL = new StringBuilder(EventRestContentProvider.SERVERAPIPATH);

        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                baseURL.append("/event");
                break;
            case EVENTS_ID:
                baseURL.append("/event");
                baseURL.append("/").append(uri.getLastPathSegment());
                object = this.toEvent(values);
                break;
            case COMMENTS:
                baseURL.append("/comment");
                break;
            case COMMENTS_ID:
                baseURL.append("/comment");
                baseURL.append("/").append(uri.getLastPathSegment());
                object = this.toComment(values);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        baseURL.append("/update");

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
