package br.com.steam.views.user

import androidx.lifecycle.*
import br.com.steam.data.daos.UserDao
import br.com.steam.data.models.UserSteam
import br.com.steam.data.models.UserWithGames
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class UserViewModel(private val dao: UserDao): ViewModel(){

    val allUsers: LiveData<List<UserSteam>> = dao.getUsers().asLiveData()
    val allUserWithGames: LiveData<List<UserWithGames>> = dao.getUsersWithGames().asLiveData()

    fun insert(userSteam: UserSteam){
        viewModelScope.launch {
            dao.insert(userSteam)
        }
    }

    fun update(userSteam: UserSteam){
        viewModelScope.launch {
            dao.update(userSteam)
        }
    }

    fun delete(userSteam: UserSteam){
        viewModelScope.launch {
            dao.delete(userSteam)
        }
    }

    fun getUser(id: Int): UserSteam{
        allUsers.value?.forEach{
            if(id == it.userId){
                return it
            }
        }
        return UserSteam(
            -1,
            "",
            "",
            0.0,
            0
        )
    }
}

class UserSteamViewModeFactory(private val dao: UserDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java))
            return UserViewModel(dao) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}