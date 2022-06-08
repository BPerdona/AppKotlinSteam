package br.com.steam.views.category

import androidx.lifecycle.*
import br.com.steam.data.daos.CategoryDao
import br.com.steam.data.models.Category
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class CategoriesViewModel(private val dao: CategoryDao): ViewModel(){

    val allCategories: LiveData<List<Category>> = dao.getCategory().asLiveData()

    fun insert(category: Category){
        viewModelScope.launch {
            dao.insert(category)
        }
    }

    fun update(category: Category){
        viewModelScope.launch {
            dao.update(category)
        }
    }

    fun delete(category: Category){
        viewModelScope.launch{
            dao.delete(category)
        }
    }

    fun getCategory(id: Int): Category{
        allCategories.value?.forEach{
            if(id == it.categoryId){
                return it
            }
        }
        return Category(
            -1,
            "",
            ""
        )
    }

    fun getLastIndex(): Int{
        return allCategories.value?.get(allCategories.value?.size?:0)?.categoryId?:0
    }
}

class CategoryViewModelFactory(private val dao: CategoryDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CategoriesViewModel::class.java))
            return CategoriesViewModel(dao) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}