package prueba.pruebamoviesfirebase.movieList.data.local.movie

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Clase que representa la base de datos de películas utilizando la biblioteca Room.
 *
 * @property entities Lista de clases que representan entidades dentro de la base de datos.
 *                   En este caso, solo se incluye la entidad [MovieEntity].
 * @property version Número de versión de la base de datos. Al incrementar este número, se puede
 *                   realizar una migración de la base de datos si es necesario.
 */
@Database(
    entities = [MovieEntity::class],// Lista de todas las entidades incluidas en la base de datos.
    version = 1// Versión de la base de datos. Incrementar este número para realizar una migración.
)
abstract class MovieDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
}