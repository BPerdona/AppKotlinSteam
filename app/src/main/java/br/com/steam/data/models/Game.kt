package br.com.steam.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.RESTRICT
import androidx.room.PrimaryKey

@Entity(
    tableName = "game",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("categoryId"),
            childColumns = arrayOf("gameCategoryId"),
            onDelete = RESTRICT,
            onUpdate = CASCADE
        )
    ]
)
data class Game(
    @PrimaryKey(autoGenerate = true)
    val gameId: Int = 0,
    val name: String,
    val description: String,
    val score: String,
    val price: String,
    val gameCategoryId: Int
)