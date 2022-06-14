package br.com.steam

import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.steam.data.daos.GameUserCrossRefDao
import br.com.steam.data.models.Category
import br.com.steam.data.models.Game
import br.com.steam.data.models.GameUserCrossRef
import br.com.steam.data.models.UserSteam
import br.com.steam.ui.theme.SteamTheme
import br.com.steam.views.category.*
import br.com.steam.views.game.*
import br.com.steam.views.user.*
import br.com.steam.views.usergame.AddRemoveGamesUser
import br.com.steam.views.usergame.AddRemoveGamesUserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //User
        val userSteamViewModel: UserViewModel by viewModels<UserViewModel>{
            UserSteamViewModeFactory(
                (this.applicationContext as SteamApplication).steamDatabase.userSteamDao()
            )
        }
        val saveEditUserViewModel: SaveEditUserViewModel by viewModels()
        saveEditUserViewModel.setIndex(userSteamViewModel.getLastIndex())

        //Category
        val categoriesViewModel: CategoriesViewModel by viewModels<CategoriesViewModel>{
            CategoryViewModelFactory(
                (this.applicationContext as SteamApplication).steamDatabase.categoryDao()
            )
        }
        val saveEditCategoriesViewModel: SaveEditCategoriesViewModel by viewModels()
        saveEditCategoriesViewModel.setIndex(categoriesViewModel.getLastIndex())

        //Game
        val gameViewModel: GameViewModel by viewModels<GameViewModel> {
            GameViewModelFactory(
                (this.applicationContext as SteamApplication).steamDatabase.gameDao()
            )
        }
        val saveEditGameViewModel: SaveEditGameViewModel by viewModels()
        saveEditGameViewModel.setIndex(gameViewModel.getLastIndex())

        //GameUserViewModel
        val gameUserViewModel: GameUserViewModel by viewModels<GameUserViewModel>{
            GameUserViewModelFactory(
                (this.applicationContext as SteamApplication).steamDatabase.gameUserCrossRefDao()
            )
        }
        val addRemoveGamesUserViewModel: AddRemoveGamesUserViewModel by viewModels()


        setContent {
            SteamTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SteamApp(
                        userSteamViewModel,
                        saveEditUserViewModel,
                        categoriesViewModel,
                        saveEditCategoriesViewModel,
                        gameViewModel,
                        saveEditGameViewModel,
                        gameUserViewModel,
                        addRemoveGamesUserViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun SteamApp(
    userViewModel: UserViewModel,
    saveEditUserViewModel: SaveEditUserViewModel,
    categoriesViewModel: CategoriesViewModel,
    saveEditCategoriesViewModel: SaveEditCategoriesViewModel,
    gameViewModel: GameViewModel,
    saveEditGameViewModel: SaveEditGameViewModel,
    gameUserViewModel: GameUserViewModel,
    addRemoveGamesUserViewModel: AddRemoveGamesUserViewModel
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(80.dp)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavScreens.forEach{ botNavScreen ->
                    BottomNavigationItem(
                        icon ={ Icon(
                            modifier = Modifier.size(50.dp),
                            painter = painterResource(id = botNavScreen.icon),
                            contentDescription = stringResource(id = botNavScreen.name)
                        )},
                        label = { Text(text = stringResource(id = botNavScreen.name))},
                        selected = currentDestination?.hierarchy?.any{
                            it.route == botNavScreen.route
                        } == true,
                        onClick = {
                            navController.navigate(botNavScreen.route){
                                popUpTo(navController.graph.startDestinationId){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = Screen.UsersScreen.route
        ){
            composable(Screen.CategoriesScreen.route){
               CategoriesScreen(categoriesViewModel, navController)
            }
            composable(Screen.GamesScreen.route){
                GamesScreen(gameViewModel, navController)
            }
            composable(Screen.UsersScreen.route){
                UserScreen(userViewModel, navController)
            }
            composable(
                route = "users/{userId}",
                arguments = listOf(navArgument("userId"){
                    defaultValue = -1
                    type = NavType.IntType
                })
            ){
                val userId = it.arguments?.getInt("userId") ?: -1
                val user = userViewModel.getUserWithGames(userId)
                SaveEditUser(
                    user,
                    userViewModel,
                    navController,
                    saveEditUserViewModel
                )
            }
            composable(
                route = "users/{userId}/addGame",
                arguments = listOf(navArgument("userId"){
                    defaultValue = -1
                    type = NavType.IntType
                })
            ){
                val userId = it.arguments?.getInt("userId") ?: -1
                val userWithGames = userViewModel.getUserWithGames(userId)
                AddRemoveGamesUser(
                    userWithGames,
                    gameViewModel,
                    navController,
                    gameUserViewModel,
                    addRemoveGamesUserViewModel
                )
            }
            composable(
                route = "categories/{categoryId}",
                arguments = listOf(navArgument("categoryId"){
                    defaultValue = -1
                    type = NavType.IntType
                })
            ){
                val category = categoriesViewModel.getCategory(
                    it.arguments?.getInt("categoryId") ?: -1
                )
                SaveEditCategory(
                    category = category,
                    categoryViewModel = categoriesViewModel,
                    navController = navController,
                    saveEditCategoriesViewModel = saveEditCategoriesViewModel
                )
            }
            composable(
                route = "games/{gameId}",
                arguments = listOf(navArgument("gameId"){
                    defaultValue = -1
                    type = NavType.IntType
                })
            ){
                val game = gameViewModel.getGame(
                    it.arguments?.getInt("gameId") ?: -1
                )
                SaveEditGame(
                    game,
                    gameViewModel,
                    navController,
                    saveEditGameViewModel,
                    categoriesViewModel
                )
            }
        }
    }
}

private val bottomNavScreens = listOf(
    Screen.CategoriesScreen,
    Screen.GamesScreen,
    Screen.UsersScreen
)

sealed class Screen(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val name: Int,
){
    object CategoriesScreen: Screen("categories", R.drawable.chart, R.string.categories)
    object GamesScreen: Screen("games", R.drawable.gamepad, R.string.games)
    object UsersScreen: Screen("users", R.drawable.user, R.string.users)
}
