package prueba.pruebamoviesfirebase.movieList.domain.repository

import kotlinx.coroutines.flow.Flow
import prueba.pruebamoviesfirebase.movieList.domain.model.Movie
import prueba.pruebamoviesfirebase.movieList.util.Resource

/**
 * Interfaz que define las operaciones para obtener información sobre listas de películas y detalles de películas individuales.
 */
interface MovieListRepository {

    /**
     * Obtiene la lista de películas desde una fuente remota (API) o local (base de datos).
     *
     * @param forceFetchFromRemote Indica si se debe forzar la obtención de datos desde la fuente remota.
     * @param category Categoría de las películas que se están buscando.
     * @param page Número de página de resultados para paginación.
     * @return Un [Flow] de [Resource] que contiene la lista de películas o un mensaje de error.
     */
    suspend fun getMovieList(
        // forceFetchFromRemote --> Esto hace que al scrollear al final lo pongamos como nulo y no siga recopilando datos
        //También hace que al cerrar y abrir la app no vuelva a pedir los datos de la API, si no de la database interna que generamos
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>>

    /**
     * Obtiene los detalles de una película por su identificador único.
     *
     * @param id Identificador único de la película.
     * @return Un [Flow] de [Resource] que contiene los detalles de la película o un mensaje de error.
     */
    suspend fun getMovie(id: Int): Flow<Resource<Movie>>
}