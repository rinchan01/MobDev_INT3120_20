package com.example.planegame.database;
import android.provider.BaseColumns;

public final class SkinDatabase {
    private SkinDatabase() {
    }

    public static class SkinEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String SCORE = "score";
    }
}