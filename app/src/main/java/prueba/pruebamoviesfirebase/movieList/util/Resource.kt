package prueba.pruebamoviesfirebase.movieList.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : Resource<T>(data) //Cuando encontramos con éxito respuesta y una lista de movies
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message) //No se ha encontrado respuesta o hubo un error
    class Loading<T>(val isLoading: Boolean = true) : Resource<T>(null) //Mientras esperamos una respuesta o lista
}