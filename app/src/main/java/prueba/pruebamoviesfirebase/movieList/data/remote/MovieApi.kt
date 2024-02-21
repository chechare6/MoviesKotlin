package prueba.pruebamoviesfirebase.movieList.data.remote

import prueba.pruebamoviesfirebase.movieList.data.remote.respond.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz para interactuar con la API de películas.
 */
interface MovieApi {

    /**
     * Obtiene una lista de películas según la categoría especificada.
     *
     * @param category La categoría de las películas que se están buscando.
     * @param page Número de página de resultados para paginación.
     * @param apiKey Clave de la API para autenticación. Por defecto, se utiliza la clave especificada en [API_KEY].
     * @return [MovieListDto] que contiene la lista de películas y detalles de paginación.
     */
    @GET("movie/{category}") //<-- de retrofit, entre los parentesis especificamos los datos/ruta que buscamos
    suspend fun getMoviesList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ) : MovieListDto

    /**
     * Acompañante que contiene valores constantes utilizados por la interfaz.
     */
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/" // URL base de la API
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"// URL base para las imágenes
        const val API_KEY = "edfd0840fcf1c72800b436a8934a62c9"// Clave de la API
    }
}