package br.com.steam.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class GameWithCategory(
    @Embedded val game: Game,
    @Relation(
        parentColumn = "gameCategoryId",
        entityColumn = "categoryId"
    )
    val category: Category
)
