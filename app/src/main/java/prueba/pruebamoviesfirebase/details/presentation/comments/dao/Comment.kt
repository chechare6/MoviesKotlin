package prueba.pruebamoviesfirebase.details.presentation.comments.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Comment(
    val user: String,
    val movieId: Int,
    val content: String,
    val dateAdded: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)