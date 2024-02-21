package prueba.pruebamoviesfirebase.movieList.data.remote.respond

/**
 * Clase que representa la estructura de datos de una lista de películas recibida desde una fuente externa, como una API.
 *
 * @property page Número de página actual en la lista de resultados.
 * @property results Lista de objetos [MovieDto] que representan las películas de la página actual.
 * @property total_pages Número total de páginas disponibles en la lista.
 * @property total_results Número total de resultados disponibles en la lista.
 */
data class MovieListDto(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)