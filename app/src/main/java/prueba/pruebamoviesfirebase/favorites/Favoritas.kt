package prueba.pruebamoviesfirebase.favorites

/**
 * Clase Data se usara para crear el objeto pelicula favorita,
 * que se guardara en nuestra Realtime Database como un archivo json.
 *
 * @param key Valor tipo String que usaremos como un id para esa pelicula guardada.
 * @param uid Valor tipo String que ussaremos para guardar la id del usuario.
 * @param uid Valor tipo String que ussaremos para guardar la id de la película.
 */
data class Favorita (
    val key: String? = null,
    val uid: String = "",
    val peliId: String = ""
)