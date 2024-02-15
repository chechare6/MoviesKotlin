package prueba.pruebamoviesfirebase.movieList.data.local.movie

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity

    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    suspend fun getMovieListByCategory(category: String): List<MovieEntity>

    // FAVORITO
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(movie: MovieEntity)

    // FAVORITO
    @Delete
    suspend fun deleteFavoriteMovie(movie: MovieEntity)

    // FAVORITO
    @Query("SELECT * FROM MovieEntity WHERE isFavorite = 1")
    suspend fun getAllFavoriteMovies(): List<MovieEntity>
}