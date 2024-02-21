package prueba.pruebamoviesfirebase.movieList.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import prueba.pruebamoviesfirebase.movieList.presentation.components.MovieItem
import prueba.pruebamoviesfirebase.movieList.util.Category

/**
 * Composable para la pantalla de películas populares.
 *
 * @param movieListState Estado de la lista de películas.
 * @param navController Controlador de navegación para la navegación entre pantallas.
 * @param onEvent Función de devolución de llamada para manejar eventos relacionados con la lista de películas.
 */
@Composable
fun PopularMoviesScreen(
    movieListState: MovieListState,
    navController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit
) {
    // Si la lista de películas populares está vacía, muestra un indicador de progreso.
    if (movieListState.popularMovieList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Si hay películas en la lista, muestra un LazyVerticalGrid con las películas.
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ) {
            items(movieListState.popularMovieList.size) { index ->
                MovieItem(
                    movie = movieListState.popularMovieList[index],
                    popular = true,
                    navHostController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Si se alcanza el final de la lista y no se están cargando más datos, se solicita paginación.
                if (index >= movieListState.popularMovieList.size - 1 && !movieListState.isLoading) {
                    onEvent(MovieListUiEvent.Paginate(Category.POPULAR))
                }
            }
        }
    }
}