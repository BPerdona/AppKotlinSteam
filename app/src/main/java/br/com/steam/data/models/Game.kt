package br.com.steam.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val gameId: Int = 0,
    val name: String,
    val description: String,
    val score: String,
    val price: String,
    val gameCategoryId: Int
)