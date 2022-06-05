package br.com.steam.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithGames(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "gameCategoryId"
    )
    val games: List<Game>
)
