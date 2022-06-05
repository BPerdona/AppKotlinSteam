package br.com.steam.data.daos

import androidx.room.*
import br.com.steam.data.models.Category
import br.com.steam.data.models.CategoryWithGames
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM category")
    fun getCategory(): Flow<List<Category>>

    @Transaction
    @Query("SELECT * FROM category")
    fun getCategoryWithGames(): Flow<List<CategoryWithGames>>
}