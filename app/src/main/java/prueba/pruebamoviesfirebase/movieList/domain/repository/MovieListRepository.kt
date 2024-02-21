package prueba.pruebamoviesfirebase.movieList.domain.repository

import kotlinx.coroutines.flow.Flow
import prueba.pruebamoviesfirebase.movieList.domain.model.Movie
import prueba.pruebamoviesfirebase.movieList.util.Resource

interface MovieListRepository {
    //Pedimos la lista de peliculas
    suspend fun getMovieList(
        // forceFetchFromRemote --> Esto hace que al scrollear al final lo pongamos como nulo y no siga recopilando datos
        //También hace que al cerrar y abrir la app no vuelva a pedir los datos de la API, si no de la database interna que generamos
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>>

    //Pedimos una película en concreto
    suspend fun getMovie(id: Int): Flow<Resource<Movie>>
}