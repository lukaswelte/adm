package com.adm.meetup.event;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.net.Uri;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
	public HttpClient client = new DefaultHttpClient();
	private static final String SERVER = "http://meetup.apiary.io/";
    private static final String AUTHORITY = "com.adm.meetup.event.EventManager";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, "event", 1);
        //sURIMatcher.addURI(AUTHORITY, "workspaces/projects/#",2);
    }

    public Event getEventById(Long id) {
        throw new UnsupportedOperationException();
    }

    public ArrayList<Event> getEvents() {
        throw new UnsupportedOperationException();
    }

    public void updateEvent(Event event) {
        throw new UnsupportedOperationException();
    }

    public void createEvent(Event event) {
    	HttpPut request = new HttpPut(EventManager.SERVER + "event"); 
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(); 
		
		//pairs.add(new BasicNameValuePair("name", name));
		//pairs.add(new BasicNameValuePair("score", score));
		try {

			request.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try { 
			HttpResponse response = client.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }

    public void deleteEvent(Event event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        int match = sURIMatcher.match(uri);

        StringBuilder baseurl= new StringBuilder(EventManager.SERVER);
        switch(match) {
            case    1:
                baseurl.append("event");
                break;
        }

        String s = "{"
                + "  \"query\": \"Pizza\", "
                + "  \"locations\": [ 94043, 90210 ] "
                + "}";
        JSONObject object;
        String[] columnNames = {"id", "name"};
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

            row.add("asd", e);
        } catch (Exception e) {

        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}