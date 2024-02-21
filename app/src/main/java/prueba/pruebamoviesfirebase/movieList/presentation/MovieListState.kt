package prueba.pruebamoviesfirebase.movieList.presentation

import prueba.pruebamoviesfirebase.movieList.domain.model.Movie

/**
 * Clase de estado que representa el estado de la lista de películas en la aplicación.
 *
 * @property isLoading Indica si la lista de películas se está cargando actualmente.
 * @property popularMovieListPage Página actual de la lista de películas populares.
 * @property nowPlayingMovieListPage Página actual de la lista de películas que se están reproduciendo actualmente.
 * @property favoritesMovieListPage Página actual de la lista de películas favoritas.
 * @property isScreen Indica la pantalla actual que se está mostrando.
 * @property popularMovieList Lista de películas populares.
 * @property nowPlayingMovieList Lista de películas que se están reproduciendo actualmente.
 * @property favoritesMovieList Lista de películas marcadas como favoritas.
 */
data class MovieListState(
    val isLoading: Boolean = false,

    val popularMovieListPage: Int = 1,
    val nowPlayingMovieListPage: Int = 1,
    val favoritesMovieListPage: Int = 1,

    val isScreen: Int = 0,

    val popularMovieList: List<Movie> = emptyList(),
    val nowPlayingMovieList: List<Movie> = emptyList(),
    val favoritesMovieList: List<Movie> = emptyList()
)