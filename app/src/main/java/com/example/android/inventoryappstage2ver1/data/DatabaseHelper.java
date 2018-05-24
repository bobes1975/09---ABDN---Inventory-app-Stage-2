package com.example.android.inventoryappstage2ver1.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventoryappstage2ver1.data.DatabaseContract.GameEntry;

/**
 * Database helper for Games app. Manages database creation and version management.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "games.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link DatabaseHelper}.
     *
     * @param context of the app
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the games table
        String SQL_CREATE_GAMES_TABLE = "CREATE TABLE " + GameEntry.TABLE_NAME + " ("
                + GameEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GameEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + GameEntry.COLUMN_CATEGORY + " INTEGER NOT NULL, "
                + GameEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + GameEntry.COLUMN_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + GameEntry.COLUMN_SUPPLIER + " TEXT NOT NULL, "
                + GameEntry.COLUMN_SUPPLIER_PHONE + " INTEGER NOT NULL DEFAULT 0,"
                + GameEntry.COLUMN_SUPPLIER_EMAIL + " TEXT );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_GAMES_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
