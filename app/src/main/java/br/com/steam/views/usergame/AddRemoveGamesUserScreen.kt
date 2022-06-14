package br.com.steam.views.usergame

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.steam.data.models.Game
import br.com.steam.data.models.GameUserCrossRef
import br.com.steam.data.models.UserWithGames
import br.com.steam.views.game.GameViewModel
import br.com.steam.views.user.GameUserViewModel

@Composable
fun AddRemoveGamesUser(
    user: UserWithGames,
    gameViewModel: GameViewModel,
    navController: NavController,
    gameUserViewModel: GameUserViewModel,
    addRemoveGamesUserViewModel: AddRemoveGamesUserViewModel
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("users"){
                    popUpTo("users")
                }
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Confirm")
            }
        }
    ) {
        val games by gameViewModel.allGames.observeAsState(listOf())
        addRemoveGamesUserViewModel.gamesInUser.value = user.games

        Column() {
            GameList(
                addRemoveGamesUserViewModel,
                games,
                gameUserViewModel,
                user
            )
        }
    }
}

@Composable
fun GameList(
    addRemoveGamesUserViewModel: AddRemoveGamesUserViewModel,
    games: List<Game>,
    gameUserViewModel: GameUserViewModel,
    user: UserWithGames
){
    Card(
        modifier = Modifier
            .padding(3.dp)
            .animateContentSize(
                spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ){
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Lista de Jogos:",
                    style = MaterialTheme.typography.h5.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
    LazyColumn(){
        items(games){
            var exists = false
            if (user.games.contains(it))
                exists=true
            GameItemOff(
                it,
                exists,
                addGame = {
                    gameUserViewModel.insert(GameUserCrossRef(user.userSteam.userId, it.gameId))
                },
                removeGame = {
                    gameUserViewModel.delete(GameUserCrossRef(user.userSteam.userId, it.gameId))
                }
            )
        }
    }
}

@Composable
fun GameItemOff(
    game: Game,
    exists: Boolean,
    addGame: () -> Unit,
    removeGame: () -> Unit
){
    var gameStatus by remember{mutableStateOf(exists)}
    Card(
        modifier = Modifier
            .padding(3.dp)
            .clickable {
                if(gameStatus){
                    removeGame()
                }else{
                    addGame()
                }
                gameStatus = !gameStatus
            }
            .animateContentSize(
                spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ){
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray)
                    .padding(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier=Modifier
                        .padding(17.dp)
                        .weight(1.0F),
                    text = game.name,
                    style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Normal),
                )
                if(gameStatus)
                    Icon(
                        modifier = Modifier.padding(end = 10.dp),
                        imageVector = Icons.Default.Check,
                        tint = Color.Green,
                        contentDescription = "Add game in player"
                    )
                else
                    Icon(
                        modifier = Modifier.padding(end = 10.dp),
                        imageVector = Icons.Default.Close,
                        tint = Color.Red,
                        contentDescription = "Remove game of player"
                    )
            }
        }
    }
}