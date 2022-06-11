package br.com.steam.views.category

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
import br.com.steam.data.models.CategoryWithGames
import br.com.steam.data.models.Game

@Composable
fun CategoriesScreen(
    categoriesViewModel:CategoriesViewModel,
    navController: NavController
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("categories/-1")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Category")
            }
        }
    ) {
       val categories by categoriesViewModel.allCategoriesWithGames.observeAsState(listOf())

        Column() {
            CategoryList(categories, navController)
        }
    }
}

@Composable
fun CategoryList(
    categories: List<CategoryWithGames>,
    navController: NavController
){
    LazyColumn(){
        items(categories){
            CategoryItem(it.category, it.games){
                navController.navigate("categories/${it.category.categoryId}")
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    games: List<Game>,
    edit: () -> Unit
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
                        text = "${category.categoryId}",
                        style = MaterialTheme.typography.h3
                            .copy(color = Color.White, fontWeight = FontWeight.Normal)
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f),
                    text = category.name,
                    style = MaterialTheme.typography.h5
                        .copy(color = Color.White ,fontWeight = FontWeight.Bold)
                )
                if(expanded){
                    Icon(
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 20.dp, 0.dp)
                            .size(32.dp)
                            .clickable { edit() },
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
                                .padding(bottom = 5.dp)
                                .weight(1f),
                            text = "Nome: ${category.name}",
                            style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .weight(1f),
                            text = "ID: ${category.categoryId}",
                            style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                    }
                    Text(
                        text = "Bio:",
                        style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${category.description}",
                        style = MaterialTheme.typography.subtitle2.copy(color = Color.LightGray),
                        modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp)
                    )
                    Text(
                        modifier = Modifier
                            .padding(bottom = 8.dp),
                        text = "Jogos com essa categoria: ",
                        style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                    CategoryGames(games = games)
                }
            }
        }
    }
}

@Composable
fun CategoryGames(games: List<Game>){
    if(games.isNullOrEmpty()){
        Text(
            text = "Nenhum jogo cadastrado nessa Categoria!!!",
            style = MaterialTheme.typography.subtitle2.copy(color = Color.Red, fontWeight = FontWeight.Bold)
        )
    }else{
        games.forEach{
            Text(
                text = "- ${it.name}"
            )
        }
    }
}
