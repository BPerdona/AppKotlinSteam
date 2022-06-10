package br.com.steam.views.game

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.steam.data.models.Category
import br.com.steam.data.models.Game
import br.com.steam.views.category.CategoriesViewModel

@Composable
fun SaveEditGame(
    game: Game,
    gameViewModel: GameViewModel,
    navController: NavController,
    saveEditGameViewModel: SaveEditGameViewModel,
    categoriesViewModel: CategoriesViewModel
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if(game.gameId == -1){
                    saveEditGameViewModel.insert(gameViewModel::insert)
                }else{
                    saveEditGameViewModel.update(game.gameId, gameViewModel::update)
                }
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Confirm Game"
                )
            }
        }
    ) {
        saveEditGameViewModel.name.value = game.name
        saveEditGameViewModel.description.value = game.description
        saveEditGameViewModel.score.value = game.score
        saveEditGameViewModel.price.value = game.price
        saveEditGameViewModel.gameCategoryId.value = game.gameCategoryId

        GameForm(
            saveEditGameViewModel,
            gameViewModel,
            game,
            categoriesViewModel,
        ){
            navController.navigate("games")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GameForm(
    saveEditGameViewModel: SaveEditGameViewModel,
    gameViewModel: GameViewModel,
    game: Game,
    categoriesViewModel: CategoriesViewModel,
    navBack: () -> Unit
){
    val name = saveEditGameViewModel.name.observeAsState()
    val description = saveEditGameViewModel.description.observeAsState()
    val score = saveEditGameViewModel.score.observeAsState()
    val price = saveEditGameViewModel.price.observeAsState()
    val gameCategoryId = saveEditGameViewModel.gameCategoryId.observeAsState()

    val categories by categoriesViewModel.allCategories.observeAsState(listOf())

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
                    saveEditGameViewModel.name.value = it
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
                    saveEditGameViewModel.description.value = it
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp, start = 6.dp, end = 6.dp, top = 6.dp),
                label = {
                    Text(text = "Nota")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Yellow,
                    focusedBorderColor = Color.Yellow
                ),
                value = "${score.value}",
                onValueChange = {
                    saveEditGameViewModel.score.value = it
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp, start = 6.dp, end = 6.dp, top = 6.dp),
                label = {
                    Text(text = "Price")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Yellow,
                    focusedBorderColor = Color.Yellow
                ),
                value = "${price.value}",
                onValueChange = {
                    saveEditGameViewModel.price.value = it
                }
            )
            var expanded by remember { mutableStateOf(false)}
            var selectedGame by remember { mutableStateOf(categoriesViewModel.getCategory(gameCategoryId.value?:0).name)}

            ExposedDropdownMenuBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp, start = 6.dp, end = 0.dp, top = 6.dp),
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    readOnly= true,
                    value = selectedGame,
                    onValueChange = { },
                    label = { Text(text = "Selecione uma Categoria")},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedLabelColor = Color.Yellow,
                        focusedTrailingIconColor = Color.Yellow,
                        focusedIndicatorColor = Color.Yellow,
                        backgroundColor = Color.Black
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach{
                        DropdownMenuItem(onClick = {
                            selectedGame = it.name
                            saveEditGameViewModel.gameCategoryId.value = it.categoryId
                            expanded = false
                        }) {
                            Text(text = it.name)
                        }
                    }
                }
            }
        }
        if (game.gameId != -1) {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    gameViewModel.delete(game)
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




