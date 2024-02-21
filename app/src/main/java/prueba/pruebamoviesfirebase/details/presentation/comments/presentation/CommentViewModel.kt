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

/**
 * ViewModel encargado de gestionar la lógica de presentación y el estado relacionado con los comentarios.
 * Utiliza Hilt para la inyección de dependencias y Room para la persistencia de datos.
 *
 * @property dao El Data Access Object (DAO) para las operaciones de base de datos relacionadas con los comentarios.
 */
@HiltViewModel
class CommentViewModel @Inject constructor (
    private val dao: CommentDao
) : ViewModel() {
    /**
     * Un [MutableStateFlow] que indica si la lista de comentarios está ordenada por fecha de adición.
     */
    private val isSortedByDateAdded = MutableStateFlow(true)

    /**
     * Un [StateFlow] que contiene la lista actual de comentarios. Se actualiza automáticamente en
     * respuesta a cambios en el estado, la ordenación o la base de datos.
     */
    private var comments =
        isSortedByDateAdded.flatMapLatest { _ ->
            dao.getComments(movieId = initialState.value.movieId.value)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    /**
     * Un [MutableStateFlow] que representa el estado inicial de la aplicación para la sección de comentarios.
     */
    private val initialState = MutableStateFlow(CommentState())

    /**
     * Un [StateFlow] que combina el estado inicial, la información de ordenación y la lista de comentarios
     * para proporcionar el estado actualizado de la interfaz de usuario.
     */
    val state =
        combine(initialState, isSortedByDateAdded, comments) { state, _, comments ->
            state.copy(
                comments = comments
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CommentState())

    /**
     * Maneja eventos relacionados con los comentarios, como eliminar o guardar un comentario.
     *
     * @param event El evento que desencadena una acción en el ViewModel.
     */
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