package prueba.pruebamoviesfirebase.details.presentation.comments.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de acceso a datos (DAO) para la entidad Comment.
 *
 * Define las operaciones de base de datos necesarias para gestionar los comentarios
 * en la aplicación, permitiendo insertar, eliminar y consultar comentarios de forma asincrónica.
 */
@Dao
interface CommentDao  {

    /**
     * Inserta un nuevo comentario en la base de datos o actualiza uno existente.
     *
     * Si el comentario ya existe (basado en su clave primaria), este será reemplazado
     * por el nuevo. Esta operación es útil para mantener actualizados los comentarios
     * sin realizar operaciones separadas de inserción y actualización.
     *
     * @param comment El comentario a insertar o actualizar.
     */
    @Upsert
    suspend fun upsertComment(comment: Comment)

    /**
     * Elimina un comentario específico de la base de datos.
     *
     * @param comment El comentario a eliminar. Se identifica por su clave primaria.
     */
    @Delete
    suspend fun deleteComment(comment: Comment)

    /**
     * Recupera una lista de comentarios asociados a una película específica, ordenados por fecha de adición.
     *
     * Esta consulta devuelve un Flow que emite la lista de comentarios cada vez que hay un cambio
     * en los comentarios de la película especificada. Es ideal para mantener una UI reactiva que
     * se actualiza automáticamente con los últimos comentarios.
     *
     * @param movieId El identificador de la película para la cual se quieren obtener los comentarios.
     * @return Un Flow que emite listas de comentarios.
     */
    @Query("SELECT * FROM comment WHERE movieId = :movieId ORDER BY dateAdded")
    fun getComments(movieId: Int) : Flow<List<Comment>>
}