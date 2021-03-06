package br.com.steam.views.game

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.steam.data.models.Category
import br.com.steam.data.models.Game
import br.com.steam.data.models.GameWithCategory
import br.com.steam.views.category.CategoriesViewModel

@Composable
fun GamesScreen(
    gameViewModel: GameViewModel,
    navController: NavController
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("games/-1")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Game")
            }
        }
    ) {
        val gamesWithCategory by gameViewModel.allGameWithCategory.observeAsState(listOf())
        Column() {
            GameList(gamesWithCategory, navController)
        }
    }
}

@Composable
fun GameList(
    gamesWithCategory: List<GameWithCategory>,
    navController: NavController
){
    LazyColumn(){
        items(gamesWithCategory){
            GameItem(it){
                navController.navigate("games/${it.game.gameId}")
            }
        }
    }
}

@Composable
fun GameItem(
    gameWithCategory: GameWithCategory,
    toDetail: () -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(3.dp)
            .clickable {
                expanded = !expanded
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
                    .background(color = Color.DarkGray)
                    .padding(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .border(
                            width = 4.dp,
                            color = Color.Black,
                            shape = CircleShape
                        )
                        .size(75.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                    ,
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "${gameWithCategory.game.gameId}",
                        style = MaterialTheme.typography.h3
                            .copy(color = Color.White, fontWeight = FontWeight.Normal)
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f),
                    text = "${gameWithCategory.game.name}",
                    style = MaterialTheme.typography.h5
                        .copy(color = Color.White ,fontWeight = FontWeight.Bold)
                )
                if(expanded){
                    Icon(
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 20.dp, 0.dp)
                            .size(32.dp)
                            .clickable { toDetail() },
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Yellow
                    )
                }
            }
            if(expanded){
                Column(
                    modifier = Modifier
                        .border(
                            width = 5.dp,
                            color = Color.DarkGray,
                            shape = RoundedCornerShape(2.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 1.dp)
                                .weight(1f),
                            text = "ID: ${gameWithCategory.game.gameId}",
                            style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .weight(0.9f),
                            text = "Pre??o: ${gameWithCategory.game.price}",
                            style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 1.dp)
                                .weight(1f),
                            text = "Nome: ${gameWithCategory.game.name}",
                            style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .weight(0.9f),
                            text = "Nota: ${gameWithCategory.game.score}",
                            style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 10.dp),
                            text = "Categoria:",
                            style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                        CategoryCard(gameWithCategory.category)
                    }
                    Text(
                        text = "Bio:",
                        style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${gameWithCategory.game.description}",
                        style = MaterialTheme.typography.subtitle2.copy(color = Color.LightGray),
                        modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Category
){
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .background(Color.White)
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)
                    .padding(4.dp)
            ) {
                Text(
                    text = "${category.name}",
                    style = MaterialTheme.typography.subtitle2.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)
                    .padding(4.dp)
            ) {
                Text(
                    text = "Descri????o: ${category.description}",
                    style = MaterialTheme.typography.subtitle2.copy(color = Color.LightGray),
                )
            }
        }
    }
}

