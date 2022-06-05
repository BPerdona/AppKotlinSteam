package br.com.steam.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithGames(
    @Embedded val userSteam: UserSteam,
    @Relation(
        parentColumn = "userId",
        entityColumn = "gameId",
        associateBy = Junction(GameUserCrossRef::class)
    )
    val games: List<Game>
)
