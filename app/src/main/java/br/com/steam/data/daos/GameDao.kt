package br.com.steam.data.daos

import androidx.room.*
import br.com.steam.data.models.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insert(game: Game)

    @Update
    suspend fun update(game: Game)

    @Delete
    suspend fun delete(game: Game)

    @Query("SELECT * FROM game")
    fun getGames(): Flow<List<Game>>
}