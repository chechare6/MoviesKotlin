package prueba.pruebamoviesfirebase.movieList.data.local.movie

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * Interfaz de acceso a datos (DAO) para la entidad MovieEntity.
 * Define las operaciones de base de datos necesarias para gestionar las películas.
 */
@Dao
interface MovieDao {

    /**
     * Inserta o actualiza una lista de películas en la base de datos.
     *
     * @param movieList Lista de objetos MovieEntity que se insertarán o actualizarán en la base de datos.
     */
    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    /**
     * Obtiene una película por su identificador único.
     *
     * @param id El identificador único de la película.
     * @return La película correspondiente al identificador proporcionado.
     */
    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity

    /**
     * Obtiene una lista de películas filtradas por categoría.
     *
     * @param category La categoría por la cual se filtran las películas.
     * @return Una lista de objetos MovieEntity que pertenecen a la categoría especificada.
     */
    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    suspend fun getMovieListByCategory(category: String): List<MovieEntity>
}