package prueba.pruebamoviesfirebase.details.presentation.comments.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa un comentario realizado por un usuario sobre una película específica.
 *
 * Esta entidad forma parte de la base de datos local de la aplicación, gestionada por Room,
 * y se utiliza para almacenar y recuperar comentarios asociados a películas.
 *
 * @property user El nombre de usuario o identificador del autor del comentario.
 * @property movieId Identificador único de la película sobre la cual se hace el comentario.
 *                Este campo se supone que está relacionado con una entidad de película
 *                en la base de datos, aunque dicha relación debe gestionarse manualmente
 *                o a través de consultas, ya que Room no soporta relaciones directas en objetos.
 * @property content El contenido textual del comentario realizado por el usuario.
 * @property dateAdded La fecha y hora en la que se agregó el comentario, representada como
 *                un valor Long
 * @property id El identificador único del comentario en la base de datos. Este campo es auto-generado
 *                por Room cuando el comentario es insertado en la base de datos, proporcionando
 *                así un mecanismo para identificar de manera única cada comentario.
 */
@Entity
data class Comment(
    val user: String,
    val movieId: Int,
    val content: String,
    val dateAdded: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)