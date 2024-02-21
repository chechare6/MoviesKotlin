package prueba.pruebamoviesfirebase.movieList.util

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Barra de puntuación que muestra estrellas llenas, medias y vacías según la puntuación proporcionada.
 *
 * @param modifier [Modifier] para aplicar a la barra de puntuación.
 * @param starsModifier [Modifier] para aplicar a cada estrella individual.
 * @param rating Puntuación que determina el número de estrellas llenas.
 * @param stars Número total de estrellas en la barra.
 * @param starsColor Color de las estrellas.
 */
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    starsModifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starsColor: Color = Color.Yellow,
) {

    val filledStars = kotlin.math.floor(rating).toInt()
    val unfilledStars = (stars - kotlin.math.ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))

    Row(modifier = modifier) {
        // Estrellas llenas
        repeat(filledStars) {
            Icon(
                modifier = starsModifier,
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = starsColor
            )
        }
        // Estrella media (si es necesario)
        if (halfStar) {
            Icon(
                modifier = starsModifier,
                imageVector = Icons.Rounded.StarHalf,
                contentDescription = null,
                tint = starsColor
            )
        }
        // Estrellas vacías
        repeat(unfilledStars) {
            Icon(
                modifier = starsModifier,
                imageVector = Icons.Rounded.StarOutline,
                contentDescription = null,
                tint = starsColor
            )
        }
    }
}