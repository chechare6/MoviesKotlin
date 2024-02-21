package prueba.pruebamoviesfirebase.details.presentation.comments.dao

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Define la base de datos para la aplicación con Room, incluyendo todas las entidades y versiones.
 *
 * Esta clase abstracta extiende RoomDatabase, sirviendo como el punto central para acceder a las
 * operaciones de la base de datos proporcionadas por las interfaces DAO.
 *
 * @property dao Proporciona acceso al [CommentDao] para realizar operaciones de base de datos
 * sobre la entidad [Comment].
 */
@Database(
    entities = [Comment::class],// Lista de todas las entidades incluidas en la base de datos.
    version = 1// Versión de la base de datos. Incrementar este número para realizar una migración.
)

abstract class CommentDatabase : RoomDatabase() {
    abstract val dao: CommentDao
}