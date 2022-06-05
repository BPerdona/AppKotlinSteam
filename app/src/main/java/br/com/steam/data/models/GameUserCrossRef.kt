package br.com.steam.data.models

import androidx.room.Entity

@Entity(primaryKeys = ["userId","gameId"])
data class GameUserCrossRef(
    val userId: Int,
    val songId: Int
)
