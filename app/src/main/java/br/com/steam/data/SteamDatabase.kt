package br.com.steam.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.steam.data.daos.CategoryDao
import br.com.steam.data.daos.GameDao
import br.com.steam.data.daos.UserDao
import br.com.steam.data.models.Category
import br.com.steam.data.models.Game
import br.com.steam.data.models.GameUserCrossRef
import br.com.steam.data.models.UserSteam

@Database(
    entities = [Category::class, Game::class, UserSteam::class, GameUserCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class SteamDatabase: RoomDatabase(){

    abstract fun categoryDao(): CategoryDao
    abstract fun gameDao(): GameDao
    abstract fun userSteamDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: SteamDatabase? = null

        fun getInstance(context: Context): SteamDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    SteamDatabase::class.java,
                    "steam_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}