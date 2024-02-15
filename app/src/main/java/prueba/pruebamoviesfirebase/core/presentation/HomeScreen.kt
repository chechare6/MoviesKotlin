package prueba.pruebamoviesfirebase.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import prueba.pruebamoviesfirebase.R
import prueba.pruebamoviesfirebase.login.utils.AuthManager
import prueba.pruebamoviesfirebase.movieList.presentation.FavoritesMoviesScreen
import prueba.pruebamoviesfirebase.movieList.presentation.MovieListUiEvent
import prueba.pruebamoviesfirebase.movieList.presentation.MovieListViewModel
import prueba.pruebamoviesfirebase.movieList.presentation.PopularMoviesScreen
import prueba.pruebamoviesfirebase.movieList.presentation.UpcomingMoviesScreen
import prueba.pruebamoviesfirebase.movieList.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController(), auth: AuthManager = AuthManager()) {

    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = movieListViewModel.movieListState.collectAsState().value
    val bottomNavController = rememberNavController()
    var showDialog by remember { mutableStateOf(false) }
    val onLogoutConfirmed: () -> Unit = {
        auth.signOut()
        navController.navigate(Screen.LogIn.route) {
            popUpTo(Screen.LogIn.route) {
                inclusive = true
            }
        }
    }

    Scaffold(bottomBar = {
        BottomNavigationBar(
            bottomNavController = bottomNavController, onEvent = movieListViewModel::onEvent
        )
    }, topBar = {
        TopAppBar(
            title = {
                Text(
                    text = when(movieListState.isScreen)
                    {
                        0 -> stringResource(R.string.popular_movies)
                        1 -> stringResource(R.string.upcoming_movies)
                        else -> stringResource(R.string.favorite_movies)
                    },
                    fontSize = 20.sp
                )
            },
            modifier = Modifier.shadow(2.dp),
            colors = TopAppBarDefaults.smallTopAppBarColors(
                    MaterialTheme.colorScheme.inverseOnSurface
            ),
            actions = {
                IconButton(
                    onClick = {
                        showDialog = true
                    }
                ) {
                    Icon(
                        Icons.Outlined.ExitToApp,
                        contentDescription = "Cerrar sesión"
                    )
                }
            }
        )
    }) {
        Box(modifier = Modifier.padding(it)) {
            // Muestra el diálogo de confirmación de cierre de sesión si showDialog es true.
            if (showDialog) {
                LogoutDialog(onConfirmLogout = {
                    onLogoutConfirmed()
                    showDialog = false
                }, onDismiss = { showDialog = false })
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.PopularMovieList.route
            ) {
                composable(Screen.PopularMovieList.route) {
                    PopularMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
                composable(Screen.FavoritesMovieList.route) {
                    FavoritesMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
                composable(Screen.UpcomingMovieList.route) {
                    UpcomingMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
            }
        }
    }

}


@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController, onEvent: (MovieListUiEvent) -> Unit
) {

    val items = listOf(
        BottomItem(
            title = stringResource(R.string.popular),
            icon = Icons.Rounded.Movie
        ), BottomItem(
            title = stringResource(R.string.upcoming),
            icon = Icons.Rounded.Upcoming
        ), BottomItem(
            title = stringResource(R.string.favorites),
            icon = Icons.Rounded.Favorite
        )
    )

    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        Row(
            modifier = Modifier.run { background(MaterialTheme.colorScheme.inverseOnSurface) }
        ) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(selected = selected.intValue == index, onClick = {
                    selected.intValue = index
                    when (selected.intValue) {
                        0 -> {
                            onEvent(MovieListUiEvent.Navigate(0))
                            bottomNavController.popBackStack()
                            bottomNavController.navigate(Screen.PopularMovieList.route)
                        }

                        1 -> {
                            onEvent(MovieListUiEvent.Navigate(1))
                            bottomNavController.popBackStack()
                            bottomNavController.navigate(Screen.UpcomingMovieList.route)
                        }

                        2 -> {
                            onEvent(MovieListUiEvent.Navigate(2))
                            bottomNavController.popBackStack()
                            bottomNavController.navigate(Screen.FavoritesMovieList.route)
                        }
                    }
                }, icon = {
                    Icon(
                        imageVector = bottomItem.icon,
                        contentDescription = bottomItem.title,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }, label = {
                    Text(
                        text = bottomItem.title, color = MaterialTheme.colorScheme.onBackground
                    )
                })
            }
        }
    }

}

data class BottomItem(
    val title: String, val icon: ImageVector
)

@Composable
fun LogoutDialog(onConfirmLogout: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar sesión") },
        text = { Text("¿Estás seguro que deseas cerrar sesión?") },
        confirmButton = {
            Button(
                onClick = onConfirmLogout
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}