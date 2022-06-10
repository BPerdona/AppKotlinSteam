package br.com.steam.views.category

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.steam.data.models.Category
import br.com.steam.views.user.castDouble
import br.com.steam.views.user.castInt

@Composable
fun SaveEditCategory(
    category: Category,
    categoryViewModel: CategoriesViewModel,
    navController: NavController,
    saveEditCategoriesViewModel: SaveEditCategoriesViewModel
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if(category.categoryId == -1){
                    saveEditCategoriesViewModel.insert(categoryViewModel::insert)
                } else {
                    saveEditCategoriesViewModel.update(categoryViewModel::update)
                }
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "Confirm")
            }
        }
    ) {
        saveEditCategoriesViewModel.name.value = category.name
        saveEditCategoriesViewModel.description.value = category.description

        CategoryForm(
            saveEditCategoriesViewModel,
            categoryViewModel,
            category
        ){
            navController.navigate("categories")
        }
    }
}

@Composable
fun CategoryForm(
    saveEditCategoriesViewModel: SaveEditCategoriesViewModel,
    categoryViewModel: CategoriesViewModel,
    category: Category,
    navBack: () -> Unit
) {
    val name = saveEditCategoriesViewModel.name.observeAsState()
    val description = saveEditCategoriesViewModel.description.observeAsState()

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp, start = 6.dp, end = 6.dp, top = 6.dp),
                label = {
                    Text(text = "Nome")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Yellow,
                    focusedBorderColor = Color.Yellow
                ),
                value = "${name.value}",
                onValueChange = {
                    saveEditCategoriesViewModel.name.value = it
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp, start = 6.dp, end = 6.dp),
                label = {
                    Text(text = "Descrição")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Yellow,
                    focusedBorderColor = Color.Yellow
                ),
                value = "${description.value}",
                onValueChange = {
                    saveEditCategoriesViewModel.description.value = it
                }
            )
        }
        if (category.categoryId != -1) {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    categoryViewModel.delete(category)
                    navBack()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}