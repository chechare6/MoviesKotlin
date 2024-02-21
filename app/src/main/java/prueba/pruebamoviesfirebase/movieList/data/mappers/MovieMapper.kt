package prueba.pruebamoviesfirebase.movieList.data.mappers

import prueba.pruebamoviesfirebase.movieList.data.local.movie.MovieEntity
import prueba.pruebamoviesfirebase.movieList.data.remote.respond.MovieDto
import prueba.pruebamoviesfirebase.movieList.domain.model.Movie

/**
 * Convierte un objeto [MovieDto] a una entidad [MovieEntity] asignándole una categoría específica.
 *
 * @param category La categoría asignada a la película.
 * @return Una instancia de [MovieEntity] con los datos del [MovieDto].
 */
fun MovieDto.toMovieEntity(
    category: String
): MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        id = id ?: -1,
        original_title = original_title ?: "",
        video = video ?: false,

        category = category,

        genre_ids = try {
            genre_ids?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        }
    )
}

/**
 * Convierte una entidad [MovieEntity] a un objeto [Movie] asignándole una categoría específica.
 *
 * @param category La categoría asignada a la película.
 * @return Una instancia de [Movie] con los datos de [MovieEntity].
 */
fun MovieEntity.toMovie(
    category: String
): Movie {
    return Movie(
        backdrop_path = backdrop_path,
        original_language = original_language,
        overview = overview,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        vote_average = vote_average,
        popularity = popularity,
        vote_count = vote_count,
        video = video,
        id = id,
        adult = adult,
        original_title = original_title,

        category = category,

        genre_ids = try {
            genre_ids.split(",").map { it.toInt() }
        } catch (e: Exception) {
            listOf(-1, -2)
        }
    )
}