package prueba.pruebamoviesfirebase.movieList.util

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

/**
 * Calcula el color promedio de una [ImageBitmap] y devuelve un color ligeramente más oscuro.
 *
 * @param imageBitmap [ImageBitmap] de la cual se calculará el color promedio.
 * @return [Color] que representa el color promedio ajustado a un tono más oscuro.
 */
@Composable
fun getAverageColor(imageBitmap: ImageBitmap): Color {
    var averageColor by remember { mutableStateOf(Color.Transparent) }

    // Este bloque se ejecutará cuando el composable se vuelva activo
    LaunchedEffect(Unit) {
        // Convierte la ImageBitmap a un Bitmap de Android
        val compatibleBitmap = imageBitmap.asAndroidBitmap()
            .copy(Bitmap.Config.ARGB_8888, false)

        // Recupera los píxeles del Bitmap compatible
        val pixels = IntArray(compatibleBitmap.width * compatibleBitmap.height)
        compatibleBitmap.getPixels(
            pixels, 0, compatibleBitmap.width, 0, 0,
            compatibleBitmap.width, compatibleBitmap.height
        )

        var redSum = 0
        var greenSum = 0
        var blueSum = 0

        // Calcula la suma de los valores RGB
        for (pixel in pixels) {
            val red = android.graphics.Color.red(pixel)
            val green = android.graphics.Color.green(pixel)
            val blue = android.graphics.Color.blue(pixel)

            redSum += red
            greenSum += green
            blueSum += blue
        }

        // Calcula los valores RGB promedio
        val pixelCount = pixels.size
        val averageRed = redSum / pixelCount
        val averageGreen = greenSum / pixelCount
        val averageBlue = blueSum / pixelCount

        // Establece el color promedio como resultado
        averageColor = Color(averageRed, averageGreen, averageBlue)
    }

    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(averageColor.toArgb(), hsl)

    // Disminuye el componente de luminosidad en una cantidad deseada
    val darkerLightness = hsl[2] - 0.1f // Ajusta la cantidad para hacerlo más oscuro

    // Crea un nuevo color con el componente de luminosidad modificado
    val darkerColor = ColorUtils.HSLToColor(
        floatArrayOf(
            hsl[0],
            hsl[1], darkerLightness
        )
    )

    return Color(darkerColor)
}