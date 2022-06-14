package br.com.steam.views.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.steam.data.daos.GameUserCrossRefDao
import br.com.steam.data.models.GameUserCrossRef
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class GameUserViewModel(private val dao: GameUserCrossRefDao): ViewModel(){

    fun insert(gameUserCrossRef: GameUserCrossRef){
        viewModelScope.launch {
            dao.insert(gameUserCrossRef)
        }
    }

    fun delete(userCrossRef: GameUserCrossRef){
        viewModelScope.launch{
            dao.delete(userCrossRef)
        }
    }

    fun update(userCrossRef: GameUserCrossRef){
        viewModelScope.launch{
            dao.update(userCrossRef)
        }
    }
}

class GameUserViewModelFactory(private val dao: GameUserCrossRefDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameUserViewModel::class.java))
            return GameUserViewModel(dao) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}