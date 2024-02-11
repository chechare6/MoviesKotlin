package prueba.pruebamoviesfirebase.details.presentation

import prueba.pruebamoviesfirebase.movieList.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)