package br.com.steam.views.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.steam.data.models.Category

class SaveEditCategoriesViewModel : ViewModel(){

    private val _categoryId: MutableLiveData<Int> = MutableLiveData()
    val name: MutableLiveData<String> = MutableLiveData()
    val description: MutableLiveData<String> = MutableLiveData()

    fun setIndex(index: Int){
        _categoryId.value = index
    }

    fun insert(
        insertCategory: (Category) -> Unit
    ){
        val newCategory = Category(
            _categoryId.value?:return,
            name.value?:return,
            description.value?:return
        )
        insertCategory(newCategory)
        var newIndex = _categoryId.value ?: return
        _categoryId.value=newIndex+1
        name.value = ""
        description.value = ""
    }

    fun update(
        id: Int,
        updateCategory: (Category) -> Unit
    ){
        val category = Category(
            _categoryId.value?:return,
            name.value?:return,
            description.value?:return
        )
        updateCategory(category)
    }
}