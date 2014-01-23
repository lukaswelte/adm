package com.adm.meetup.event;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EventDatabase extends SQLiteOpenHelper {
    private static final String TAG = "EventDatabase";

    private static final String DATABASE_NAME = "ic_event_sidebar.db";
    private static final int DATABASE_VERSION = 16;

    public EventDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "DB construct");
    }

    public class Tables {
        public class Events {
            final static String TABLE = "ic_event_sidebar";

            public class Columns {
                final static String ID = "id";
                final static String NAME = "name";
                final static String DESCRIPTION = "description";
                final static String LOCATION = "location";
                final static String DATE = "date";
                final static String DUE_DATE = "due_date";
                final static String SOURCE = "source";
                final static String TYPE = "type";
            }
        }

        public class Comments {
            final static String TABLE = "event_comment";

            public class Columns {
                final static String ID = "id";
                final static String EVENT_ID = "eventId";
                final static String USER_ID = "userId";
                final static String COMMENT = "comment";
                final static String DATE = "date";
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "creating database");
        String query = "CREATE TABLE " + Tables.Events.TABLE + " ("
                + Tables.Events.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Tables.Events.Columns.NAME + " TEXT NOT NULL, "
                + Tables.Events.Columns.LOCATION + " TEXT NULL, "
                + Tables.Events.Columns.DUE_DATE + " TEXT NOT NULL, "
                + Tables.Events.Columns.DATE + " TEXT NOT NULL, "
                + Tables.Events.Columns.SOURCE + " TEXT NULL, "
                + Tables.Events.Columns.DESCRIPTION + " TEXT NULL, "
                + Tables.Events.Columns.TYPE + " BLOB NULL, "
                + "UNIQUE (" + Tables.Events.Columns.ID + ") ON CONFLICT REPLACE)";
        db.execSQL(query);

        query = "CREATE TABLE " + Tables.Comments.TABLE + " ("
                + Tables.Comments.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Tables.Comments.Columns.EVENT_ID + " INTEGER NOT NULL, "
                + Tables.Comments.Columns.USER_ID + " INTEGER NOT NULL, "
                + Tables.Comments.Columns.COMMENT + " TEXT NOT NULL, "
                + Tables.Comments.Columns.DATE + " TEXT NOT NULL, "
                + "UNIQUE (" + Tables.Comments.Columns.ID + ") ON CONFLICT REPLACE)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            Log.d(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.Events.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.Comments.TABLE);

            // create tables
            onCreate(db);
        }

    }


}