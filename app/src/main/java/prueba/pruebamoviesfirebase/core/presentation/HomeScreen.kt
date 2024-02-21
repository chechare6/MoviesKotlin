package prueba.pruebamoviesfirebase.core.presentation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.LocalMovies
import androidx.compose.material.icons.rounded.Movie
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import prueba.pruebamoviesfirebase.R
import prueba.pruebamoviesfirebase.favorites.RealtimeManager
import prueba.pruebamoviesfirebase.login.utils.AuthManager
import prueba.pruebamoviesfirebase.movieList.presentation.FavoritesMoviesScreen
import prueba.pruebamoviesfirebase.movieList.presentation.MovieListUiEvent
import prueba.pruebamoviesfirebase.movieList.presentation.MovieListViewModel
import prueba.pruebamoviesfirebase.movieList.presentation.PopularMoviesScreen
import prueba.pruebamoviesfirebase.movieList.presentation.now_playingMoviesScreen
import prueba.pruebamoviesfirebase.movieList.util.Screen

/**
 * Composable que representa la pantalla principal de la aplicación.
 *
 * @param analytics Administrador de análisis para rastreo de eventos.
 * @param auth Administrador de autenticación para gestionar la información del usuario.
 * @param navigation Controlador de navegación para gestionar la navegación entre pantallas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    // Controlador de navegación para la pantalla principal.
    navController: NavHostController = rememberNavController(),
    context: Context,
    auth: AuthManager = AuthManager()
) {
    // Controlador del Realtime Database de Firebase.
    val realtime = RealtimeManager()

    // Obtiene una instancia de MovieListViewModel utilizando Hilt.
    val movieListViewModel = hiltViewModel<MovieListViewModel>()

    // Recolecta el estado del listado de películas desde el ViewModel.
    val movieListState = movieListViewModel.movieListState.collectAsState().value

    // Crea un NavController para la navegación entre composables.
    val bottomNavController = rememberNavController()

    // Estado para mostrar o no el diálogo de confirmación de cierre de sesión.
    var showDialog by remember { mutableStateOf(false) }

    // Obtiene la información del usuario actual.
    val user = auth.getCurrentUser()

    // Función para llevar a cabo el cierre de sesión.
    val onLogoutConfirmed: () -> Unit = {
        auth.signOut()
        navController.navigate(Screen.LogIn.route) {
            popUpTo(Screen.LogIn.route) {
                inclusive = true
            }
        }
    }

    // Diseño de la pantalla principal utilizando el Scaffold de Material Design.
    Scaffold(bottomBar = {
        BottomNavigationBar(
            bottomNavController = bottomNavController, onEvent = movieListViewModel::onEvent
        )
    }, topBar = {
        // Barra superior que incluye el título y el botón de cierre de sesión.
        TopAppBar(
            title = {
                // Contenido personalizado con la foto de perfil y la información del usuario.
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Si hay una foto de perfil, mostrarla.
                    if (user?.photoUrl != null){
                        //TODO: Mostrar la foto de perfil si hacemos el login con Google
                    }else{
                        // Mostrar una foto de perfil predeterminada.
                        Image(
                            painter = painterResource(R.drawable.profile),
                            contentDescription = "Foto de perfil por defecto",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                    // Información del usuario (nombre y correo electrónico).
                    Column {
                        Text(
                            text = if(!user?.displayName.isNullOrEmpty()) "Hola ${user?.displayName}" else "Bienvenidx",
                            fontSize = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if(!user?.email.isNullOrEmpty()) "${user?.email}" else "Anónimo",
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis)
                    }


                }
            },
            modifier = Modifier.shadow(2.dp),
            colors = TopAppBarDefaults.smallTopAppBarColors(
                MaterialTheme.colorScheme.inverseOnSurface
            ),
            actions = {
                // Botón de cierre de sesión.
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
        // Contenido principal de la pantalla.
        Box(modifier = Modifier.padding(it)) {
            // Muestra el diálogo de confirmación de cierre de sesión si showDialog es true.
            if (showDialog) {
                LogoutDialog(onConfirmLogout = {
                    onLogoutConfirmed()
                    showDialog = false
                }, onDismiss = { showDialog = false })
            }
        }

        // Navegacion de la barra inferior.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.PopularMovieList.route
            ) {
                // Pantalla de películas populares.
                composable(Screen.PopularMovieList.route) {
                    PopularMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
                // Pantalla de películas favoritas.
                composable(Screen.FavoritesMovieList.route) {
                    FavoritesMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent,
                        realtime = realtime,
                        authManager = auth
                    )
                }
                // Pantalla de películas en cartelera.
                composable(Screen.NowPlayingMovieList.route) {
                    now_playingMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
            }
        }
    }

}

// Barra inferior.
@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController, onEvent: (MovieListUiEvent) -> Unit
) {
    // Iconos y texto de los botones de la barra inferior.
    val items = listOf(
        BottomItem(
            title = stringResource(R.string.popular),
            icon = Icons.Rounded.Movie
        ), BottomItem(
            title = stringResource(R.string.now_playing),
            icon = Icons.Rounded.LocalMovies
        ), BottomItem(
            title = stringResource(R.string.favorites),
            icon = Icons.Rounded.Favorite
        )
    )

    // Valor seleccionado
    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        Row(
            modifier = Modifier.run { background(MaterialTheme.colorScheme.inverseOnSurface) }
        ) {
            // Vemos en que ha clicado y lo redirigimos.
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
                            bottomNavController.navigate(Screen.NowPlayingMovieList.route)
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

// Clase para BottomItem
data class BottomItem(
    val title: String, val icon: ImageVector
)

// Diálogo que se genera para confirmar el cierre de sesion
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