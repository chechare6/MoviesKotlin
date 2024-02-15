package prueba.pruebamoviesfirebase.movieList.presentation

sealed interface MovieListUiEvent {
    data class Paginate(val category: String) : MovieListUiEvent
    data class Navigate(val page: Int) : MovieListUiEvent
}