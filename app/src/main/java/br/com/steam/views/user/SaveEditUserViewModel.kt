package br.com.steam.views.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.steam.data.models.UserSteam

class SaveEditUserViewModel() : ViewModel(){

    private val _userId: MutableLiveData<Int> = MutableLiveData()
    val nickName: MutableLiveData<String> = MutableLiveData()
    val level: MutableLiveData<Int> = MutableLiveData()
    val timeInGame: MutableLiveData<Double> = MutableLiveData()
    val bio: MutableLiveData<String> = MutableLiveData()

    fun setIndex(index: Int){
        _userId.value = index
    }

    fun insert(
        insertUser: (UserSteam) -> Unit
    ){
        val newUser = UserSteam(
            _userId.value?: return,
            nickName.value?: return,
            bio.value?: return,
            timeInGame.value?: return,
            level.value?: return
        )
        insertUser(newUser)
        var newIndex = _userId.value ?: return
        _userId.value = newIndex+1
        nickName.value = ""
        bio.value = ""
        timeInGame.value = 0.0
        level.value = 0
    }

    fun update(
        id: Int,
        updateUser: (UserSteam) -> Unit
    ){
        val user = UserSteam(
            id,
            nickName.value?: return,
            bio.value?: return,
            timeInGame.value?:return,
            level.value?:return,
        )
        updateUser(user)
    }
}