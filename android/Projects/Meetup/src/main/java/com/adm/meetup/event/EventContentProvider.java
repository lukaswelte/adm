package com.adm.meetup.event;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;

/**
 * Created by jan on 18.1.14.
 */
abstract public class EventContentProvider extends ContentProvider {
    protected static final String AUTHORITY = "com.adm.meetup.event.EventManager";

    public static final int EVENTS = 100;
    public static final int EVENTS_ID = 101;

    public static final String EVENTS_URI = "content://" + EventDbContentProvider.AUTHORITY + "/event";
    public static final String EVENTS_ID_URI = "content://" + EventDbContentProvider.AUTHORITY + "/event/";

    protected static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

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
}
