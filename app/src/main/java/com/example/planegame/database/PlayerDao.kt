package com.example.planegame.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Upsert
    suspend fun upsertPlayer(player: Player)

    @Insert
    suspend fun insertPlayer(player: Player)

    @Query("SELECT * FROM player WHERE name = :name")
    suspend fun getPlayer(name: String): Player?

    @Query("SELECT * FROM player ORDER BY highScore DESC")
    fun getLeaderboard(): Flow<List<Player>>
}