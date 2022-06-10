package br.com.steam.views.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.steam.data.models.Game

class SaveEditGameViewModel: ViewModel(){

    private val _gameId: MutableLiveData<Int> = MutableLiveData()
    val name: MutableLiveData<String> = MutableLiveData()
    val description: MutableLiveData<String> = MutableLiveData()
    val score: MutableLiveData<String> = MutableLiveData()
    val price: MutableLiveData<String> = MutableLiveData()
    val gameCategoryId: MutableLiveData<Int> = MutableLiveData()

    fun setIndex(index: Int){
        _gameId.value=index
    }

    fun insert(
        insertGame: (Game) -> Unit
    ){
        val newGame = Game(
            _gameId.value?:return,
            name.value?:return,
            description.value?:return,
            score.value?:return,
            price.value?:return,
            gameCategoryId.value?:return
        )
        insertGame(newGame)
        var newIndex = _gameId.value ?: return
        _gameId.value = newIndex+1
        name.value = ""
        description.value = ""
        score.value = ""
        price.value = ""
        gameCategoryId.value = -1
    }

    fun update(
        id: Int,
        updateGame: (Game) -> Unit
    ){
        val game = Game(
            id,
            name.value?:return,
            description.value?:return,
            score.value?:return,
            price.value?:return,
            gameCategoryId.value?:return,
        )
        updateGame(game)
    }
}
