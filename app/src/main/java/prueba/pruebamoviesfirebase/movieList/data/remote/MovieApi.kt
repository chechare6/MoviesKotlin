package prueba.pruebamoviesfirebase.movieList.data.remote

import prueba.pruebamoviesfirebase.movieList.data.remote.respond.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//CON ESTA INTERFAZ PODEMOS BUSCAR PELICULAS EN LA API
//(faltaría implementación de la función en la app)
interface MovieApi {
    //unica funcion de la api que haremos
    @GET("movie/{category}") //<-- de retrofit, entre los parentesis especificamos los datos/ruta que buscamos
    suspend fun getMoviesList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ) : MovieListDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/" //enlace o direccion de la api
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = "edfd0840fcf1c72800b436a8934a62c9"
    }
}