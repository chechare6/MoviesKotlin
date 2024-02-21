package prueba.pruebamoviesfirebase.details.presentation.comments.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import prueba.pruebamoviesfirebase.details.presentation.comments.dao.Comment

data class CommentState (
    val comments: List<Comment> = emptyList(),
    var user: MutableState<String> = mutableStateOf(""),
    var movieId: MutableState<Int> = mutableIntStateOf(0),
    var content: MutableState<String> = mutableStateOf("")
)