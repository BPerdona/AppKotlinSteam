package br.com.steam.views.game

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
import br.com.steam.data.models.Game

@Composable
fun SaveEditGame(
    game: Game,
    gameViewModel: GameViewModel,
    navController: NavController,
    saveEditGameViewModel: SaveEditGameViewModel
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if(game.gameId == -1){
                    saveEditGameViewModel.insert(gameViewModel::insert)
                }else{
                    saveEditGameViewModel.update(gameViewModel::update)
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
            game
        ){
            navController.navigate("games")
        }
    }
}

@Composable
fun GameForm(
    saveEditGameViewModel: SaveEditGameViewModel,
    gameViewModel: GameViewModel,
    game: Game,
    navBack: () -> Unit
){
    val name = saveEditGameViewModel.name.observeAsState()
    val description = saveEditGameViewModel.description.observeAsState()
    val score = saveEditGameViewModel.score.observeAsState()
    val price = saveEditGameViewModel.price.observeAsState()
    val gameCategoryId = saveEditGameViewModel.gameCategoryId.observeAsState()

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




