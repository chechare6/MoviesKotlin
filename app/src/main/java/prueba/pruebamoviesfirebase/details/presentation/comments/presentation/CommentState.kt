package prueba.pruebamoviesfirebase.details.presentation.comments.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import prueba.pruebamoviesfirebase.details.presentation.comments.dao.Comment
/**
 * Representa el estado de los comentarios dentro de una interfaz de usuario específica, manteniendo
 * tanto la lista de comentarios existentes como los inputs necesarios para crear nuevos comentarios.
 *
 * Esta clase está diseñada para ser utilizada con Jetpack Compose, permitiendo un enlace reactivo
 * entre el estado de los datos y la UI, facilitando la actualización automática de la interfaz
 * cuando el estado cambia.
 *
 * @property comments Una lista inmutable de [Comment], representando los comentarios actuales.
 *                    Por defecto, es una lista vacía.
 * @property user Un estado mutable que contiene el nombre de usuario o identificador del autor
 *                del comentario. Este campo es reactivo y su modificación reflejará automáticamente
 *                los cambios en la UI que lo observa.
 * @property movieId Un estado mutable que contiene el identificador único de la película sobre
 *                   la cual se va a realizar el comentario. Al igual que con el campo `user`,
 *                   cualquier cambio en este estado se reflejará en la UI.
 * @property content Un estado mutable que contiene el texto del comentario. Este campo permite
 *                   mantener y observar cambios en el contenido del comentario a medida que el
 *                   usuario lo introduce en la interfaz.
 */
data class CommentState (
    val comments: List<Comment> = emptyList(),
    var user: MutableState<String> = mutableStateOf(""),
    var movieId: MutableState<Int> = mutableIntStateOf(0),
    var content: MutableState<String> = mutableStateOf("")
)