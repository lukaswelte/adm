package com.adm.meetup.event;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

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

        String[] columns = {"data"};
        MatrixCursor cursor = new MatrixCursor(columns);

        //String encoding = Base64.encodeToString("API_KEY".getBytes(),2);

        StringBuilder baseurl= new StringBuilder(EventManager.SERVER);
        switch(match) {
            case    1:
                baseurl.append("event");
                break;
        }

        AjaxCallback<String> cb = new AjaxCallback<String>();
        cb.url(baseurl.toString()).type(String.class).weakHandler(this,"stringCb");
        cb.header("Authorization", "Basic " + encoding);

        AQuery aq = new AQuery(getContext());

        aq.sync(cb);

        String jo = cb.getResult();
        AjaxStatus status = cb.getStatus();

        //Log.i(TAG, jo);
        //Log.i(TAG,status.toString());

        try {
            JSONObject workspaceWrapper = (JSONObject) new JSONTokener(jo).nextValue();
            JSONArray  workspaces       = workspaceWrapper.getJSONArray("data");

            for (int i = 0; i < workspaces.length(); i++) {
                String[] workspace = {workspaces.getJSONObject(i).toString()};
                cursor.addRow(workspace);
            }
        }
        catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON.", e);
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