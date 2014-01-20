package com.adm.meetup.event;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EventDatabase extends SQLiteOpenHelper {
    private static final String TAG = "EventDatabase";

    private static final String DATABASE_NAME = "event.db";
    private static final int DATABASE_VERSION = 8;

    public EventDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "DB construct");
    }

    public class Tables {
        public class Events {
            final static String TABLE = "event";

            public class Columns {
                final static String ID = "id";
                final static String NAME = "name";
                final static String DESCRIPTION = "description";
                final static String LOCATION = "location";
                final static String DATE = "date";
                final static String DUE_DATE = "due_date";
                final static String SOURCE = "source";
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "creating database");
        String query = "CREATE TABLE " + Tables.Events.TABLE + " ("
                + Tables.Events.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Tables.Events.Columns.NAME + " TEXT NULL, "
                + Tables.Events.Columns.DESCRIPTION + " TEXT NULL, "
                + Tables.Events.Columns.LOCATION + " TEXT NULL, "
                + Tables.Events.Columns.DUE_DATE + " TEXT NULL, "
                + Tables.Events.Columns.DATE + " TEXT NULL, "
                + Tables.Events.Columns.SOURCE + " TEXT NULL, "
                + "UNIQUE (" + Tables.Events.Columns.ID + ") ON CONFLICT REPLACE)";
        Log.i(TAG, query);
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if(oldVersion<newVersion){
        Log.d(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.Events.TABLE);

        // create tables
        onCreate(db);
        //}

    }

}