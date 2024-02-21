package prueba.pruebamoviesfirebase.movieList.presentation

/**
 * Interfaz sellada que representa eventos relacionados con la lista de películas en la interfaz de usuario.
 */
sealed interface MovieListUiEvent {
    /**
     * Evento para solicitar paginación de la lista de películas en una categoría específica.
     * @property category La categoría de películas para la que se solicita la paginación.
     */
    data class Paginate(val category: String) : MovieListUiEvent

    /**
     * Evento para navegar a una página específica de la lista de películas.
     * @property page El número de página al que se desea navegar.
     */
    data class Navigate(val page: Int) : MovieListUiEvent
}