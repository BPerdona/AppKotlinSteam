package br.com.steam.data.daos

import androidx.room.*
import br.com.steam.data.models.UserSteam
import br.com.steam.data.models.UserWithGames
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userSteam: UserSteam)

    @Update
    suspend fun update(userSteam: UserSteam)

    @Delete
    suspend fun delete(userSteam: UserSteam)

    @Query("SELECT * FROM user")
    fun getUsers(): Flow<List<UserSteam>>

    @Transaction
    @Query("SELECT * FROM user")
    fun getUsersWithGames(): Flow<List<UserWithGames>>
}