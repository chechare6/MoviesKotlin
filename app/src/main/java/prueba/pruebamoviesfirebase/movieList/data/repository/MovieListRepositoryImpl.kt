package prueba.pruebamoviesfirebase.movieList.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import prueba.pruebamoviesfirebase.movieList.data.local.movie.MovieDatabase
import prueba.pruebamoviesfirebase.movieList.data.mappers.toMovie
import prueba.pruebamoviesfirebase.movieList.data.mappers.toMovieEntity
import prueba.pruebamoviesfirebase.movieList.data.remote.MovieApi
import prueba.pruebamoviesfirebase.movieList.data.remote.respond.MovieDto
import prueba.pruebamoviesfirebase.movieList.domain.model.Movie
import prueba.pruebamoviesfirebase.movieList.domain.repository.MovieListRepository
import prueba.pruebamoviesfirebase.movieList.util.Resource
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Implementación del repositorio que gestiona la obtención de la lista de películas desde una fuente remota o local.
 *
 * @param movieApi Instancia de [MovieApi] para realizar solicitudes a la API de películas.
 * @param movieDatabase Instancia de [MovieDatabase] para acceder a la base de datos local de películas.
 */
class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieListRepository {

    /**
     * Obtiene la lista de películas desde una fuente remota (API) o local (base de datos).
     *
     * @param forceFetchFromRemote Indica si se debe forzar la obtención de datos desde la fuente remota.
     * @param category Categoría de las películas que se están buscando.
     * @param page Número de página de resultados para paginación.
     * @return Un [Flow] de [Resource] que contiene la lista de películas o un mensaje de error.
     */
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {

            emit(Resource.Loading(true))

            val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)

            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalMovie) {
                emit(
                    Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromApi = try {
                movieApi.getMoviesList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            val movieEntities = movieListFromApi.results.let {
                it.map { movieDto: MovieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            movieDatabase.movieDao.upsertMovieList(movieEntities)

            emit(
                Resource.Success(
                movieEntities.map { it.toMovie(category) }
            ))
            emit(Resource.Loading(false))

        }
    }

    /**
     * Obtiene los detalles de una película por su identificador único.
     *
     * @param id Identificador único de la película.
     * @return Un [Flow] de [Resource] que contiene los detalles de la película o un mensaje de error.
     */
    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {

            emit(Resource.Loading(true))

            val movieEntity = movieDatabase.movieDao.getMovieById(id)

            if (movieEntity != null) {
                emit(
                    Resource.Success(movieEntity.toMovie(movieEntity.category))
                )

                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error("Error no such movie"))

            emit(Resource.Loading(false))


        }
    }

    /* FAVORITO
    override suspend fun isFavoriteMovie(movieId: Int): Boolean {
        TODO("Not yet implemented")
    }

    // FAVORITO
    override suspend fun addToFavorite(movie: Movie) {
        TODO("Not yet implemented")
    }

    // FAVORITO
    override suspend fun removeFromFavorite(movieId: Int) {
        TODO("Not yet implemented")
    } */
}