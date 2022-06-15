package br.com.steam

import android.os.Bundle
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

    //Para começar iniciar o App com dados descomente essa linha na primeira inicialização
    // e retorne a comenta-la depois!

    //addData(categoriesViewModel, gameViewModel, userViewModel, gameUserViewModel)


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
                val canDelete = categoriesViewModel.categoryHasChildren(category.categoryId)
                SaveEditCategory(
                    cantDelete = canDelete,
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

//Função para inicializar banco de dados
fun addData(
    categoriesViewModel: CategoriesViewModel,
    gameViewModel: GameViewModel,
    userViewModel: UserViewModel,
    gameUserViewModel: GameUserViewModel,
){
    //Categorias
    val cat1 = Category(
        1,
        "FPS",
        "First Person Shooter (Tiro em Primeira Pessoa)."
    )
    categoriesViewModel.insert(cat1)
    val cat2 = Category(
        2,
        "Terror",
        "Voltado para terror psicológico e diversos JumpScares."
    )
    categoriesViewModel.insert(cat2)
    val cat3 = Category(
        3,
        "Drama",
        "Uma história incrivel está a sua espera, muitas vezes triste ou sofrida!"
    )
    categoriesViewModel.insert(cat3)
    val cat4 = Category(
        4,
        "Estratégia",
        "Voltado para pessoas que adoram pensar enquanto jogam para fazer as melhores escolhar."
    )
    categoriesViewModel.insert(cat4)
    val cat5 = Category(
        5,
        "Corrida",
        "Muita velocidade envolvida e será necesário ter bons reflexos."
    )
    categoriesViewModel.insert(cat5)
    val cat6 = Category(
        6,
        "RPG",
        "Role Play Game. Focado em criação de personagem e evoluir itens, armas e niveis."
    )
    categoriesViewModel.insert(cat6)

    //Games
    val game1 = Game(
        1,
        "Counter Strike",
        "Um time Terrorista contra um time Contra-Terrorista.",
        "8.9",
        "19.99",
        1
    )
    gameViewModel.insert(game1)
    val game2 = Game(
        2,
        "God Of War",
        "Kratos quer vingança pelo o que os deuses do Olympus fizeram com ele.",
        "9.4",
        "119.99",
        3
    )
    gameViewModel.insert(game2)
    val game3 = Game(
        3,
        "GRIS",
        "Uma história sobre uma garota e seu Luto.",
        "9.1",
        "12.50",
        3
    )
    gameViewModel.insert(game3)
    val game4 = Game(
        4,
        "Half-Life",
        "Gordon Freeman precisa sobreviver a um acidente radioativo e a invasão de alienigenas.",
        "9.8",
        "4.99",
        1
    )
    gameViewModel.insert(game4)
    val game5 = Game(
        5,
        "Silent Hill",
        "Terror Psicológico e uma experiência no purgatório.",
        "8.9",
        "19.99",
        2
    )
    gameViewModel.insert(game5)
    val game6 = Game(
        6,
        "World Of Warcraft",
        "Um universo gigantesco e fantasioso está a sua espera!",
        "9.9",
        "149.00",
        6
    )
    gameViewModel.insert(game6)

    //Usuários
    val user1 = UserSteam(
        1,
        "PrN",
        "Adoro jogos diferentes",
        2042.91,
        41,
    )
    userViewModel.insert(user1)
    val user2 = UserSteam(
        2,
        "Monster2231",
        "Just For Fun >=D",
        282.00,
        11,
    )
    userViewModel.insert(user2)
    val user3 = UserSteam(
        3,
        "SkateBoy",
        "O-<--<]:",
        1273.01,
        33,
    )
    userViewModel.insert(user3)
    val user4 = UserSteam(
        4,
        "AroundTheWorld",
        "I love play games and travels",
        12.66,
        5,
    )
    userViewModel.insert(user4)
    val user5 = UserSteam(
        5,
        "NooB69",
        "Preciso de um trabalho...",
        14542.97,
        97,
    )
    userViewModel.insert(user5)
    val user6 = UserSteam(
        6,
        "NewUser8803",
        "Hi I'm using Steam",
        0.0,
        1,
    )
    userViewModel.insert(user6)

    //GamesUser
    val gu1 = GameUserCrossRef(
        1,
        4
    )
    gameUserViewModel.insert(gu1)
    val gu2 = GameUserCrossRef(
        2,
        5
    )
    gameUserViewModel.insert(gu2)
    val gu3 = GameUserCrossRef(
        3,
        6
    )
    gameUserViewModel.insert(gu3)
    val gu4 = GameUserCrossRef(
        4,
        2
    )
    gameUserViewModel.insert(gu4)
    val gu5 = GameUserCrossRef(
        5,
        1
    )
    gameUserViewModel.insert(gu5)
    val gu6 = GameUserCrossRef(
        5,
        2
    )
    gameUserViewModel.insert(gu6)
    val gu7 = GameUserCrossRef(
        5,
        3
    )
    gameUserViewModel.insert(gu7)
}
