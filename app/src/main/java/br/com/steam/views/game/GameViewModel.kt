package br.com.steam.views.game

import androidx.lifecycle.*
import br.com.steam.data.daos.GameDao
import br.com.steam.data.models.Game
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class GameViewModel(private val dao: GameDao) : ViewModel(){

    val allGames: LiveData<List<Game>> = dao.getGames().asLiveData()

    fun insert(game: Game){
        viewModelScope.launch {
            dao.insert(game)
        }
    }

    fun update(game: Game){
        viewModelScope.launch {
            dao.update(game)
        }
    }

    fun delete(game: Game){
        viewModelScope.launch {
            dao.delete(game)
        }
    }

    fun getGame(id: Int): Game{
        allGames.value?.forEach{
            if(id == it.gameCategoryId){
                return it
            }
        }
        return Game(
            -1,
            "",
            "",
            "",
            "",
            -1
        )
    }

    fun getLastIndex(): Int{
        return allGames.value?.get(allGames.value?.size?:0)?.gameId?:0
    }
}

class GameViewModelFactory(private val dao: GameDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameViewModel::class.java))
            return GameViewModel(dao) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}