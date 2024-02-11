package prueba.pruebamoviesfirebase.movieList.data.remote.respond

import prueba.pruebamoviesfirebase.movieList.data.remote.respond.MovieDto

data class MovieListDto(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)