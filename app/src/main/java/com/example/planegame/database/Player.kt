package com.example.planegame.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey
    val name: String,
    val highScore: Int,
    val coins: Int,
    @ColumnInfo(defaultValue = "6")
    val skins: String,
    val avatarPath: String,
    val location: String
)
