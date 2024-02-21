package prueba.pruebamoviesfirebase.mapas.presentation

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import kotlinx.coroutines.tasks.await
import prueba.pruebamoviesfirebase.mapas.data.Cinema
import prueba.pruebamoviesfirebase.mapas.data.NearbyCinemasResponse
import prueba.pruebamoviesfirebase.mapas.data.PlacesClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// Composable que muestra un mapa de Google con marcadores de cines cercanos
@Composable
fun Map() {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        // Realiza una búsqueda de cines cercanos.
        val cinesCercanos = searchCinemas(placeType = "movie_theater")
        cinesCercanos?.let {
            // Itera sobre la lista de cines cercanos y agrega un marcador para cada uno.
            cinesCercanos.forEach { cinema ->
                Marker(
                    position = LatLng(cinema.geometry.location.lat, cinema.geometry.location.lng),
                    title = cinema.name
                )
            }
        }
    }
}

// Función para buscar cines cercanos basados en el tipo de lugar proporcionado.
@SuppressLint("MissingPermission")
@Composable
fun searchCinemas(
    placeType: String
):List<Cinema>? {
    // Estado para almacenar la lista de cines cercanos.
    var lugaresCercanos by remember {
        mutableStateOf<List<Cinema>?>(null)
    }
    val context = LocalContext.current
    val fusedLocationProvider = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Estado para almacenar la ubicación actual del usuario.
    var location: Location? by remember { mutableStateOf(null) }

    // LaunchedEffect para obtener la ubicación actual del usuario.
    LaunchedEffect(Unit) {
        try {
            val locationResult = fusedLocationProvider.lastLocation.await()
            location = locationResult
        } catch (e:Exception) {
            Log.e("CINE", "ERROR OBTENIENDO LA LOCALIZACIÓN || ${e.message}")
        }
    }

    // Radio de búsqueda en metros
    val radius = 10000

    // LaunchedEffect para realizar una búsqueda de lugares cercanos cuando se actualiza la ubicación.
    LaunchedEffect(location) {
        location?.let { loc ->
            fetchNearbyPlaces(
                loc.latitude.toString() + "," + loc.longitude.toString(),
                radius,
                placeType
            ) { cines ->
                // Ordena la lista de cines cercanos por distancia y toma los primeros 15.
                lugaresCercanos = cines.sortedBy { distanceBetweenPoints(
                    loc.latitude, loc.longitude,
                    it.geometry.location.lat, it.geometry.location.lng
                ) }
                    .take(15)
            }
        }
    }
    return lugaresCercanos
}

// Función para realizar una solicitud a la API de lugares para obtener cines cercanos.
fun fetchNearbyPlaces(
    location: String,
    radius: Int,
    type: String,
    onSuccess: (List<Cinema>) -> Unit
) {
    // Instancia de PlacesApi mediante el objeto singleton PlacesClient.
    val service = PlacesClient.create()

    val apiKey = "AIzaSyCu38GPCVQH8srrl1tIGgJiOrdlG0vJzpI"

    // Llama a la función de la API para obtener cines cercanos
    val call = service.getNearbyCinemas(location, radius, type, apiKey)
    call.enqueue(object: Callback<NearbyCinemasResponse> {
        override fun onResponse(
            call: Call<NearbyCinemasResponse>,
            response: Response<NearbyCinemasResponse>
        ) {
            if(response.isSuccessful) {
                // Obtiene la lista de cines cercanos de la respuesta
                val nearbyCinemas = response.body()?.results
                nearbyCinemas?.let {
                    // Llama al callback onSuccess con la lista de cines cercanos
                    onSuccess(it)
                }
            } else {
                Log.e("CINE", "Error al obtener cines cercanos: ${response.code()}")
            }
        }
        override fun onFailure(call: Call<NearbyCinemasResponse>, t: Throwable) {
            Log.e("CINE", "Error de conexión: ${t.message}", t)
        }
    })
}

// Función para calcular la distancia entre dos puntos geográficos utilizando la fórmula de Haversine.
fun distanceBetweenPoints(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    // Radio de la Tierra en kilómetros
    val earthRadius = 6371

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    // Distancia en kilómetros.
    return earthRadius * c
}