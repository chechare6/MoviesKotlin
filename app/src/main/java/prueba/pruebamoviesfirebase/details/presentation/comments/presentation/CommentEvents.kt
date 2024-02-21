package prueba.pruebamoviesfirebase.details.presentation.comments.presentation

import prueba.pruebamoviesfirebase.details.presentation.comments.dao.Comment

sealed interface CommentsEvents {
    data class DeleteComments(val comment: Comment) : CommentsEvents
    data class SaveComment(
        val user: String,
        val movieId: Int,
        val content: String
    ) : CommentsEvents
}