package br.com.steam.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserSteam(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val nickName: String,
    val bio: String,
    val timeInGame: Double,
    val level: Int
)