package br.com.steam.views.user

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.steam.data.models.UserSteam
import java.lang.Exception

@Composable
fun SaveEditUser(
    user: UserSteam,
    userViewModel: UserViewModel,
    navController: NavController,
    saveEditUserViewModel: SaveEditUserViewModel
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (user.userId == -1) {
                    saveEditUserViewModel.insert(userViewModel::insert)
                } else {
                    saveEditUserViewModel.update(user.userId, userViewModel::update)
                }
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "Confirm")
            }
        }
    ) {
        saveEditUserViewModel.nickName.value = user.nickName
        saveEditUserViewModel.bio.value = user.bio
        saveEditUserViewModel.level.value = user.level
        saveEditUserViewModel.timeInGame.value = user.timeInGame

        UserForm(
            saveEditUserViewModel,
            userViewModel,
            user,
        ){
            navController.navigate("users")
        }
    }
}

@Composable
fun UserForm(
    saveEditUserViewModel: SaveEditUserViewModel,
    userViewModel: UserViewModel,
    user: UserSteam,
    navigateBack: () -> Unit,
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
        }
        if(user.userId != -1){
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    userViewModel.delete(user)
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