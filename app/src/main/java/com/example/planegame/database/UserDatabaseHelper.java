package com.example.planegame.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "User";
    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + UserDatabase.UserEntry.TABLE_NAME + " (" +
                    UserDatabase.UserEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    UserDatabase.UserEntry.NAME + " TEXT NOT NULL," + // Assuming NAME is a text field
                    UserDatabase.UserEntry.SCORE + " INTEGER)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserDatabase.UserEntry.TABLE_NAME;


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
