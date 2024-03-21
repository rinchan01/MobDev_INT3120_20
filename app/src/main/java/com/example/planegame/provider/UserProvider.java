package com.example.planegame.provider;

import static com.example.planegame.database.UserDatabase.UserEntry.TABLE_NAME;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.planegame.database.UserDatabaseHelper;

import java.util.HashMap;

public class UserProvider extends ContentProvider {
    static final String AUTHORITY = "com.example.planegame.provider";

    static final String URL = "content://" + AUTHORITY + "/user";
    public static final Uri URI = Uri.parse(URL);
    public static final String id = "id";
    public static final String name = "name";
    public static final Integer score = 0;
    static final int uriCode = 1;
    static final UriMatcher uriMatcher;

    static {

        // to match the content URI
        // every time user access table under content provider
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // to access whole table
        uriMatcher.addURI(AUTHORITY, "users", uriCode);

        // to access a particular row
        // of the table
        uriMatcher.addURI(AUTHORITY, "users/*", uriCode);
    }

    private static HashMap<String, String> values;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        if (db != null) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = db.insert(TABLE_NAME, "", values);
        // check whether the record has been inserted or not by rowId value
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        // if rowId <= 0 then the record has not been inserted
        throw new SQLiteException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}