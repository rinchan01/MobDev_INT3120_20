package com.example.planegame.database;

import android.provider.BaseColumns;

import org.jetbrains.annotations.NotNull;

public final class UserDatabase {
    private UserDatabase() {
    }

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String ID = "id";
        @NotNull
        public static final String NAME = "name";
        public static final String SCORE = "score";
    }
}
