package prueba.pruebamoviesfirebase.movieList.data.remote.respond

/**
 * Clase que representa la estructura de datos de una película recibida desde una fuente externa, como una API.
 *
 * @property adult Indica si la película es para adultos.
 * @property backdrop_path Ruta de la imagen de fondo de la película.
 * @property genre_ids Lista de identificadores de género asociados a la película.
 * @property id Identificador único de la película.
 * @property original_language Idioma original de la película.
 * @property original_title Título original de la película.
 * @property overview Descripción general de la trama de la película.
 * @property popularity Popularidad de la película.
 * @property poster_path Ruta de la imagen del póster de la película.
 * @property release_date Fecha de lanzamiento de la película.
 * @property title Título de la película.
 * @property video Indica si la película tiene un video asociado.
 * @property vote_average Calificación promedio de la película.
 * @property vote_count Cantidad de votos recibidos por la película.
 */
data class MovieDto(
    val adult: Boolean?,
    val backdrop_path: String?,
    val genre_ids: List<Int>?,
    val id: Int?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?,
)