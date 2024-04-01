package com.example.planegame.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
@Database(
    entities = [Player::class],
    version = 1,
    exportSchema = true
)
abstract class PlayerDatabase: RoomDatabase() {

    abstract val playerDao: PlayerDao

    companion object {
        @Volatile
        private var INSTANCE: PlayerDatabase? = null

        fun getDatabase(context: Context): PlayerDatabase {
            if(INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): PlayerDatabase {
            return databaseBuilder(
                context.applicationContext,
                PlayerDatabase::class.java,
                "player_database"
            ).build()
        }

    }
}