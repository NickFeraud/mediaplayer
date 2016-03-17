package edu.fsu.cs.groupproject.radar;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.Date;

/**
 * Created by The Red Comet on 3/16/2016.
 */
public class MyContentProvider extends ContentProvider {
    public final static String DBNAME = "Locations";
    public final static String TABLE_OBJECTS = "stored_objects";
    public final static String COLUMN_LATITUDE = "latitude";
    public final static String COLUMN_LONGITUDE = "longitude";
    public final static String COLUMN_DATESTORED = "date_stored";
    public final static String COLUMN_ITEMNAME = "item_name";
    public final static String COLUMN_USERNAME = "username";
    public final static String COLUMN_LOCHINT = "location_hint";

    public static final String AUTHORITY = "edu.fsu.cs.groupproject.radar";
    public static final Uri CONTENT_URI = Uri.parse(
            "content://edu.fsu.cs.groupproject.radar/" + TABLE_OBJECTS);

    private static UriMatcher sUriMatcher;

    private MainDatabaseHelper mOpenHelper;

    private static final String SQL_CREATE_MAIN = "CREATE TABLE " +
            TABLE_OBJECTS +  // Table's name
            "(" +               // The columns in the table
            " _ID INTEGER PRIMARY KEY, " +
            COLUMN_LATITUDE +
            " DOUBLE," +
            COLUMN_LONGITUDE +
            " DOUBLE, " +
            COLUMN_DATESTORED +
            " DATETIME, " +
            COLUMN_ITEMNAME +
            " TEXT, " +
            COLUMN_USERNAME +
            " TEXT, " +
            COLUMN_LOCHINT +
            " TEXT)";

    @Override
    public boolean onCreate() {
        mOpenHelper = new MainDatabaseHelper(getContext());

        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        double latitude = values.getAsDouble(COLUMN_LATITUDE);
        double longitude = values.getAsDouble(COLUMN_LONGITUDE);
        String date_stored = values.getAsString(COLUMN_DATESTORED);
        String item_name = values.getAsString(COLUMN_ITEMNAME);
        String username = values.getAsString(COLUMN_USERNAME);
        String location_hint = values.getAsString(COLUMN_LOCHINT);

        if (latitude == 0)
            return null;

        if (longitude == 0)
            return null;

        if (date_stored.equals(""))
            return null;

        if (item_name.equals(""))
            return null;

        if (username.equals(""))
            return null;

        long id = mOpenHelper.getWritableDatabase().insert(TABLE_OBJECTS, null, values);

        return Uri.withAppendedPath(CONTENT_URI, "" + id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return mOpenHelper.getWritableDatabase().update(TABLE_OBJECTS, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mOpenHelper.getWritableDatabase().delete(TABLE_OBJECTS, selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return mOpenHelper.getReadableDatabase().query(TABLE_OBJECTS, projection, selection, selectionArgs,
                null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {
        MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_MAIN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        }
    }
}
