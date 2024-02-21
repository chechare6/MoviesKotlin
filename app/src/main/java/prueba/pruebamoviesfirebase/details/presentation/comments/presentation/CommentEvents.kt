package prueba.pruebamoviesfirebase.details.presentation.comments.presentation

import prueba.pruebamoviesfirebase.details.presentation.comments.dao.Comment

/**
 * Define los eventos relacionados con los comentarios en la aplicación.
 *
 * Esta interfaz sellada actúa como una superclase para todos los eventos de comentarios,
 * permitiendo un manejo tipo-safe de estos eventos a lo largo de la aplicación. Al ser una
 * interfaz sellada, se garantiza que todos los eventos de comentarios sean conocidos en
 * tiempo de compilación, facilitando el manejo exhaustivo de casos en la lógica de la aplicación.
 */
sealed interface CommentsEvents {
    /**
     * Representa un evento para solicitar la eliminación de un comentario específico.
     *
     * @property comment El comentario a eliminar.
     */
    data class DeleteComments(val comment: Comment) : CommentsEvents

    /**
     * Representa un evento para guardar un nuevo comentario o actualizar uno existente.
     *
     * @property user El nombre de usuario o identificador del autor del comentario.
     * @property movieId El identificador único de la película sobre la cual se hace el comentario.
     * @property content El texto del comentario propuesto por el usuario.
     */
    data class SaveComment(
        val user: String,
        val movieId: Int,
        val content: String
    ) : CommentsEvents
}