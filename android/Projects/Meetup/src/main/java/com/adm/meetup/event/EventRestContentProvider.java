package com.adm.meetup.event;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jan on 18.1.14.
 */
public class EventRestContentProvider extends EventContentProvider {
    private Context context = null;
    private static final String SERVER = "http://erasmus-meetup.herokuapp.com/";
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

        int match = sURIMatcher.match(uri);

        StringBuilder baseurl= new StringBuilder(EventRestContentProvider.SERVER);

        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                baseurl.append("event");
            case EVENTS_ID:
                baseurl.append("event"); // TODO parse ID
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        //String s = "{\"id\": 102, \"name\":\"event name\", \"date\":\"27.11.2013 13:20\", \"dueDate\":\"25.11.2013 13:20\", \"source\":\"DEFAULT\",\"types\": [{\"id\":1,\"name\": \"default\"},{\"id\":2,\"name\": \"sport\"}],\"constraints\":[{\"id\":1, \"name\": \"attendee_exact_number\"},{\"id\":2,\"name\": \"nationality_exact_number\"}],\"comments\": [{ \"id\": 1, \"user\": {\"id\":1,\"name\": \"name etc\"}, \"comment\": \"lorem ipsum...\",\"date\":\"27.11.2013 13:20\" }";

        JSONObject object;
        MatrixCursor cursor = new MatrixCursor(projection);

        try {
            HttpGet request = new HttpGet(baseurl.toString());
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            stream.close();
            String responseString = sb.toString();

            object = (JSONObject) new JSONTokener(responseString).nextValue();
            String[] row;
            row = new String[] {
                object.getString(EventDatabase.Tables.Events.Columns.ID),
                object.getString(EventDatabase.Tables.Events.Columns.NAME),
                object.getString(EventDatabase.Tables.Events.Columns.LOCATION),
                object.getString(EventDatabase.Tables.Events.Columns.DESCRIPTION),
                object.getString(EventDatabase.Tables.Events.Columns.DUE_DATE),
                object.getString(EventDatabase.Tables.Events.Columns.DATE),
                object.getString(EventDatabase.Tables.Events.Columns.SOURCE)
            };

            cursor.addRow(row);
        } catch (Exception e) {

        }

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)  {
        Uri ret;

        StringBuilder baseurl= new StringBuilder(EventRestContentProvider.SERVER);
        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                baseurl.append("event");
            case EVENTS_ID:
                baseurl.append("event/" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        ret = Uri.parse(baseurl.toString());
        HttpPost request = new HttpPost(baseurl.toString());
        JSONObject object = new JSONObject();

        try {
            object.put(EventDatabase.Tables.Events.Columns.ID, values.get(EventDatabase.Tables.Events.Columns.ID));
            object.put(EventDatabase.Tables.Events.Columns.NAME, values.get(EventDatabase.Tables.Events.Columns.NAME));
            object.put(EventDatabase.Tables.Events.Columns.LOCATION, values.get(EventDatabase.Tables.Events.Columns.LOCATION));
            object.put(EventDatabase.Tables.Events.Columns.DATE, values.get(EventDatabase.Tables.Events.Columns.DATE));
            object.put(EventDatabase.Tables.Events.Columns.DUE_DATE, values.get(EventDatabase.Tables.Events.Columns.DUE_DATE));
            object.put(EventDatabase.Tables.Events.Columns.SOURCE, values.get(EventDatabase.Tables.Events.Columns.SOURCE));
            object.put(EventDatabase.Tables.Events.Columns.DESCRIPTION, values.get(EventDatabase.Tables.Events.Columns.DESCRIPTION));
            request.setEntity(new StringEntity(object.toString()));
            HttpResponse response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("kurva", e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("kurva", "kurva 2" + e.toString());
        }

        return ret;
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
        Uri ret;
        StringBuilder baseurl= new StringBuilder(EventRestContentProvider.SERVER);
        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                baseurl.append("event");
            case EVENTS_ID:
                baseurl.append("event/" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        ret = Uri.parse(baseurl.toString());

        HttpDelete request = new HttpDelete(baseurl.toString());
        try {
            HttpResponse response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }


    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Uri ret;
        StringBuilder baseurl= new StringBuilder(EventRestContentProvider.SERVER);
        switch (sURIMatcher.match(uri)) {
            case EVENTS:
                baseurl.append("event");
            case EVENTS_ID:
                baseurl.append("event/" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        ret = Uri.parse(baseurl.toString());
        HttpPost request = new HttpPost(baseurl.toString());
        JSONObject object = new JSONObject();

        try {
            object.put(EventDatabase.Tables.Events.Columns.ID, values.get(EventDatabase.Tables.Events.Columns.ID));
            object.put(EventDatabase.Tables.Events.Columns.NAME, values.get(EventDatabase.Tables.Events.Columns.NAME));
            object.put(EventDatabase.Tables.Events.Columns.LOCATION, values.get(EventDatabase.Tables.Events.Columns.LOCATION));
            object.put(EventDatabase.Tables.Events.Columns.DATE, values.get(EventDatabase.Tables.Events.Columns.DATE));
            object.put(EventDatabase.Tables.Events.Columns.DUE_DATE, values.get(EventDatabase.Tables.Events.Columns.DUE_DATE));
            object.put(EventDatabase.Tables.Events.Columns.SOURCE, values.get(EventDatabase.Tables.Events.Columns.SOURCE));
            object.put(EventDatabase.Tables.Events.Columns.DESCRIPTION, values.get(EventDatabase.Tables.Events.Columns.DESCRIPTION));
            request.setEntity(new StringEntity(object.toString()));
            HttpResponse response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
