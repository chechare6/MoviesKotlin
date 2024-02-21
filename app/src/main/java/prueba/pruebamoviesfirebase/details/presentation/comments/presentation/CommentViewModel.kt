package prueba.pruebamoviesfirebase.details.presentation.comments.presentation

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import prueba.pruebamoviesfirebase.details.presentation.comments.dao.Comment
import prueba.pruebamoviesfirebase.details.presentation.comments.dao.CommentDao
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor (
    private val dao: CommentDao
) : ViewModel() {
    private val isSortedByDateAdded = MutableStateFlow(true)
    //lateinit var comments: StateFlow<List<Comment>> private set

    private var comments =
        isSortedByDateAdded.flatMapLatest { _ ->
            dao.getComments(movieId = initialState.value.movieId.value)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val initialState = MutableStateFlow(CommentState())
    val state =
        combine(initialState, isSortedByDateAdded, comments) { state, _, comments ->
            state.copy(
                comments = comments
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CommentState())

    fun onEvent(event: CommentsEvents) {
        when(event) {
            is CommentsEvents.DeleteComments -> {
                viewModelScope.launch {
                    dao.deleteComment(event.comment)
                }
            }
            is CommentsEvents.SaveComment -> {
                val comment = Comment(
                    user = state.value.user.value,
                    movieId = state.value.movieId.value,
                    content = state.value.content.value,
                    dateAdded = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    dao.upsertComment(comment)
                }
                initialState.update {
                    it.copy(
                        user = mutableStateOf(""),
                        movieId = mutableIntStateOf(0),
                        content = mutableStateOf("")
                    )
                }
            }
        }
    }
}