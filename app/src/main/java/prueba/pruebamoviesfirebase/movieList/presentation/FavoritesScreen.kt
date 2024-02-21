package prueba.pruebamoviesfirebase.movieList.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import prueba.pruebamoviesfirebase.favorites.RealtimeManager
import prueba.pruebamoviesfirebase.login.utils.AuthManager

//SCREEN DE LA PANTALLA DE FAVORITOS

@Composable
fun FavoritesMoviesScreen(
    movieListState: MovieListState,
    navController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit,
    realtime: RealtimeManager,
    authManager: AuthManager
) {
    val favoritas by realtime.getFavoritasFlow().collectAsState(emptyList())

    if(!favoritas.isNullOrEmpty()){
        /* //GRID DONDE DEBERÍAN APARECER LAS PELÍCULAS MARCADAS COMO FAVORITAS
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ) {
            items(movieListState.favoritesMovieList.size) { index ->
                FavoritaItem(
                    favorita = movieListState.favoritesMovieList[index],
                    realtime = realtime
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (index >= movieListState.favoritesMovieList.size - 1 && !movieListState.isLoading) {
                    onEvent(MovieListUiEvent.Paginate(Category.FAVORITES))
                }
            }
        }
        */
    } else {
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No se han encontrado películas favoritas")
        }
    }
}
