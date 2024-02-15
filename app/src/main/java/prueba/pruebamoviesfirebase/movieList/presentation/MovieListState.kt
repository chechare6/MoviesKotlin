package prueba.pruebamoviesfirebase.movieList.presentation

import prueba.pruebamoviesfirebase.movieList.domain.model.Movie

data class MovieListState(
    val isLoading: Boolean = false,

    val popularMovieListPage: Int = 1,
    val upcomingMovieListPage: Int = 1,

    val isScreen: Int = 0,

    val popularMovieList: List<Movie> = emptyList(),
    val upcomingMovieList: List<Movie> = emptyList(),
    val favoritesMovieList: List<Movie> = emptyList()
)