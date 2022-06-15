package br.com.steam.views.category

import androidx.lifecycle.*
import br.com.steam.data.daos.CategoryDao
import br.com.steam.data.models.Category
import br.com.steam.data.models.CategoryWithGames
import br.com.steam.data.models.GameWithCategory
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class CategoriesViewModel(private val dao: CategoryDao): ViewModel(){

    val allCategories: LiveData<List<Category>> = dao.getCategory().asLiveData()
    val allCategoriesWithGames: LiveData<List<CategoryWithGames>> = dao.getCategoryWithGames().asLiveData()

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
        allCategoriesWithGames.value?.forEach{
            if(id == it.category.categoryId){
                return it.category
            }
        }
        return Category(
            -1,
            "",
            ""
        )
    }

    fun getLastIndex(): Int{
        return allCategoriesWithGames.value?.get(allCategoriesWithGames.value?.size?:0)?.category?.categoryId ?: 0
    }

    fun categoryHasChildren(id: Int): Boolean{
        if(id<=0)
            return false
        allCategoriesWithGames.value?.forEach {
            if(it.category.categoryId == id)
                if(it?.games.isNullOrEmpty())
                    return false
        }
        return true
    }
}

class CategoryViewModelFactory(private val dao: CategoryDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CategoriesViewModel::class.java))
            return CategoriesViewModel(dao) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}