package br.com.steam.views.user

import android.graphics.ColorSpace
import androidx.compose.animation.VectorConverter
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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.steam.data.models.UserSteam

@Composable
fun UserScreen(
    userViewModel: UserViewModel,
    navController: NavController
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("users/-1")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add User")
            }
        }
    ) {
        val users by userViewModel.allUsers.observeAsState(listOf())

        Column() {
            UserList(users, navController)
        }
    }
}

@Composable
fun UserList(
    user: List<UserSteam>,
    navController: NavController
){
    LazyColumn(){
        items(user){ user ->
            UserItem(user){
                navController.navigate("users/${user.userId}")
            }
        }
    }
}

@Composable
fun UserItem(
    user: UserSteam,
    edit: () -> Unit
){
    var expanded by remember { mutableStateOf(false)}
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
                            color = Color.Green,
                            shape = CircleShape
                        )
                        .size(75.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                    ,
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "${user.level}",
                        style = MaterialTheme.typography.h3
                            .copy(color = Color.White, fontWeight = FontWeight.Normal)
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f),
                    text = user.nickName,
                    style = MaterialTheme.typography.h5
                        .copy(color = Color.White ,fontWeight = FontWeight.Bold)
                )
                if(expanded){
                    Icon(
                        modifier = Modifier
                            .padding(0.dp,0.dp,20.dp,0.dp)
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
                                .padding(bottom = 1.dp)
                                .weight(1f),
                            text = "ID: ${user.userId}",
                            style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .weight(0.9f),
                            text = "Level: ${user.level}",
                            style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 6.dp)
                                .weight(1f),
                            text = "Tempo de Jogo: ${user.timeInGame}",
                            style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                    }
                    Text(
                        text = "Bio:",
                        style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${user.bio}",
                        style = MaterialTheme.typography.subtitle2.copy(color = Color.LightGray),
                        modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp)
                    )
                    Text(
                        text = "Jogos de ${user.nickName}:",
                        style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                    //TODO: Adicionar todos os jogos do usuario
                }
            }
        }
    }
}