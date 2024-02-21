package prueba.pruebamoviesfirebase.details.presentation

import prueba.pruebamoviesfirebase.movieList.domain.model.Movie

/**
 * Representa el estado de la pantalla de detalles de una película en la aplicación.
 *
 * @property isLoading Indica si se están cargando datos de la película.
 * @property movie Contiene la información detallada de la película. Puede ser nulo si no se ha cargado
 *                o si hubo un error al recuperar los detalles de la película.
 */
data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)