package prueba.pruebamoviesfirebase.details.presentation.comments.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Comment::class],
    version = 1
)

abstract class CommentDatabase : RoomDatabase() {
    abstract val dao: CommentDao
}