package br.com.steam.views.user

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.steam.R
import br.com.steam.data.models.Game
import br.com.steam.data.models.UserSteam
import br.com.steam.data.models.UserWithGames
import kotlinx.coroutines.launch
import java.lang.Exception

@Composable
fun SaveEditUser(
    user: UserWithGames,
    userViewModel: UserViewModel,
    navController: NavController,
    saveEditUserViewModel: SaveEditUserViewModel
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (user.userSteam.userId == -1) {
                    saveEditUserViewModel.insert(userViewModel::insert)
                } else {
                    saveEditUserViewModel.update(user.userSteam.userId, userViewModel::update)
                }
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "Confirm")
            }
        }
    ) {
        saveEditUserViewModel.nickName.value = user.userSteam.nickName
        saveEditUserViewModel.bio.value = user.userSteam.bio
        saveEditUserViewModel.level.value = user.userSteam.level
        saveEditUserViewModel.timeInGame.value = user.userSteam.timeInGame

        UserForm(
            saveEditUserViewModel,
            userViewModel,
            user,
            saveBeforeGames = { saveEditUserViewModel.update(user.userSteam.userId, userViewModel::insert)},
            navigateBack = { navController.navigate("users") }
        ){
            navController.navigate("users/${user.userSteam.userId}/addGame")
        }
    }
}

@Composable
fun UserForm(
    saveEditUserViewModel: SaveEditUserViewModel,
    userViewModel: UserViewModel,
    user: UserWithGames,
    navigateBack: () -> Unit,
    saveBeforeGames: () -> Unit,
    addGames: () -> Unit
){
    val nickName = saveEditUserViewModel.nickName.observeAsState()
    val bio = saveEditUserViewModel.bio.observeAsState()
    val level = saveEditUserViewModel.level.observeAsState()
    val timeInGame = saveEditUserViewModel.timeInGame.observeAsState()

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
                    Text(text = "Nick Name")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Yellow,
                    focusedBorderColor = Color.Yellow
                ),
                value = "${nickName.value}",
                onValueChange = {
                    saveEditUserViewModel.nickName.value = it
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp, start = 6.dp, end = 6.dp),
                label = {
                    Text(text = "Level")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Yellow,
                    focusedBorderColor = Color.Yellow
                ),
                value = "${level.value}",
                onValueChange = {
                    saveEditUserViewModel.level.value = castInt(it)
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp, start = 6.dp, end = 6.dp),
                label = {
                    Text(text = "Time in Game")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Yellow,
                    focusedBorderColor = Color.Yellow
                ),
                value = "${timeInGame.value}",
                onValueChange = {
                    saveEditUserViewModel.timeInGame.value = castDouble(it)
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp, start = 6.dp, end = 6.dp),
                label = {
                    Text(text = "Bio")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Yellow,
                    focusedBorderColor = Color.Yellow
                ),
                value = "${bio.value}",
                onValueChange = {
                    saveEditUserViewModel.bio.value = it
                }
            )
            var expanded by remember{ mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .padding(bottom = 3.dp, start = 6.dp, end = 6.dp, top = 3.dp)
                    .clickable {
                        expanded = !expanded
                    }
                    .animateContentSize(
                        spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
            ){
                if(user.userSteam.userId != -1 )
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.DarkGray)
                                .padding(0.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .weight(1F),
                                text = "Jogos de ${saveEditUserViewModel.nickName.value}",
                                style = MaterialTheme.typography.subtitle1.copy(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                            if(expanded){
                                Icon(
                                    modifier = Modifier
                                        .weight(0.1F)
                                        .padding(9.dp),
                                    painter = painterResource(id = R.drawable.caret_up),
                                    contentDescription = "Arrow Down",
                                    tint = Color.Yellow
                                )
                            }else{
                                Icon(
                                    modifier = Modifier
                                        .weight(0.1F)
                                        .padding(9.dp),
                                    painter = painterResource(id = R.drawable.caret_down),
                                    contentDescription = "Arrow Down",
                                    tint = Color.Yellow
                                )
                            }
                        }
                        if(expanded){
                            if(user.games.isNullOrEmpty()){
                                Text(
                                    modifier = Modifier
                                        .padding(6.dp),
                                    text="Esse usuário não tem jogos na conta!",
                                    style = MaterialTheme.typography.body1.copy(color = Color.Red, fontWeight = FontWeight.Bold)
                                )
                            }else{
                                Column() {
                                    user.games.forEach {
                                        Text(
                                            modifier = Modifier
                                                .padding(6.dp),
                                            text = "- ${it.name}"
                                        )
                                    }
                                }
                            }
                            Button(
                                modifier = Modifier
                                    .padding(8.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    backgroundColor = Color.Yellow
                                ),
                                onClick = {
                                    saveBeforeGames()
                                    addGames()
                                          },
                            ) {
                                Text(
                                    text="Adicionar Jogos",
                                    color = Color.Black
                                )
                            }
                        }
                    }
            }
        }
        if(user.userSteam.userId != -1){
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    userViewModel.delete(user.userSteam)
                    navigateBack()
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


fun castDouble(context: String) : Double{
    if(context.isBlank()||context.isEmpty()){
        return 0.0
    }
    try {
        return context.toDouble()
    }
    catch (e: Exception){
        return 0.0
    }
}

fun castInt(context: String) : Int{
    if(context.isBlank()||context.isEmpty()){
        return 0
    }
    try {
        return context.toInt()
    }
    catch (e: Exception){
        return 0
    }
}