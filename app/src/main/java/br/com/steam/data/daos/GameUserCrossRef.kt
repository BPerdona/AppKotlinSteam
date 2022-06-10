package br.com.steam.data.daos

import androidx.room.*
import br.com.steam.data.models.Game
import br.com.steam.data.models.GameUserCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface GameUserCrossRef {

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insert(gameUserCrossRef: GameUserCrossRef)

    @Update
    suspend fun update(gameUserCrossRef: GameUserCrossRef)

    @Delete
    suspend fun delete(gameUserCrossRef: GameUserCrossRef)

}