package br.com.steam.views.usergame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.steam.data.models.Game

class  AddRemoveGamesUserViewModel(): ViewModel(){

    val gamesInUser: MutableLiveData<List<Game>> = MutableLiveData()

}