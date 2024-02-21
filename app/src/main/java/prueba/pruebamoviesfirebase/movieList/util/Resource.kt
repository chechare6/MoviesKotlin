package prueba.pruebamoviesfirebase.movieList.util

/**
 * Clase sellada que representa el estado de una operación que devuelve un recurso.
 *
 * @param T El tipo de datos del recurso.
 * @property data Los datos del recurso.
 * @property message El mensaje asociado con el recurso en caso de error.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Clase que representa un estado de éxito, con datos disponibles.
     *
     * @param data Los datos del recurso.
     */
    class Success<T>(data: T?) : Resource<T>(data) //Cuando encontramos con éxito respuesta y una lista de movies

    /**
     * Clase que representa un estado de error, con un mensaje descriptivo.
     *
     * @param message El mensaje de error.
     * @param data Los datos del recurso (opcional).
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message) //No se ha encontrado respuesta o hubo un error

    /**
     * Clase que representa un estado de carga, mientras se espera una respuesta.
     *
     * @param isLoading Indica si se está cargando o no.
     */
    class Loading<T>(val isLoading: Boolean = true) : Resource<T>(null) //Mientras esperamos una respuesta o lista
}