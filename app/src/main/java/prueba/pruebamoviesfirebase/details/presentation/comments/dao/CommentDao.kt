package prueba.pruebamoviesfirebase.details.presentation.comments.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao  {
    @Upsert
    suspend fun upsertComment(comment: Comment)
    @Delete
    suspend fun deleteComment(comment: Comment)
    @Query("SELECT * FROM comment WHERE movieId = :movieId ORDER BY dateAdded")
    fun getComments(movieId: Int) : Flow<List<Comment>>
}