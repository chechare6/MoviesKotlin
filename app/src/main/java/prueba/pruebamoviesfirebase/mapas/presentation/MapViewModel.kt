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

@Composable
fun Map() {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        val cinesCercanos = searchCinemas(placeType = "movie_theater")
        cinesCercanos?.let {
            cinesCercanos.forEach { cinema ->
                Marker(
                    position = LatLng(cinema.geometry.location.lat, cinema.geometry.location.lng),
                    title = cinema.name
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun searchCinemas(
    placeType: String
):List<Cinema>? {
    var lugaresCercanos by remember {
        mutableStateOf<List<Cinema>?>(null)
    }
    val context = LocalContext.current
    val fusedLocationProvider = remember { LocationServices.getFusedLocationProviderClient(context) }

    var location: Location? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        try {
            val locationResult = fusedLocationProvider.lastLocation.await()
            location = locationResult
        } catch (e:Exception) {
            Log.e("CINE", "ERROR OBTENIENDO LA LOCALIZACIÓN || ${e.message}")
        }
    }

    val radius = 10000
    LaunchedEffect(location) {
        location?.let { loc ->
            fetchNearbyPlaces(
                loc.latitude.toString() + "," + loc.longitude.toString(),
                radius,
                placeType
            ) { cines ->
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

fun fetchNearbyPlaces(
    location: String,
    radius: Int,
    type: String,
    onSuccess: (List<Cinema>) -> Unit
) {
    val service = PlacesClient.create()
    //EL PROBLEMA ESTA AQUI ^ NO ESTAMOS INSTANCIANDO BIEN
    val apiKey = "AIzaSyCu38GPCVQH8srrl1tIGgJiOrdlG0vJzpI"
    val call = service.getNearbyCinemas(location, radius, type, apiKey)
    call.enqueue(object: Callback<NearbyCinemasResponse> {
        override fun onResponse(
            call: Call<NearbyCinemasResponse>,
            response: Response<NearbyCinemasResponse>
        ) {
            if(response.isSuccessful) {
                val nearbyCinemas = response.body()?.results
                nearbyCinemas?.let {
                    onSuccess(it)
                }
            } else {
                Log.e("CINE", "Error al obtener cines cercanos: ${response.code()}")
                //Manejar error de solicitud aqui
            }
        }
        override fun onFailure(call: Call<NearbyCinemasResponse>, t: Throwable) {
            Log.e("CINE", "Error de conexión: ${t.message}", t)
            //Manejar error de conexion aqui
        }
    })
}

fun distanceBetweenPoints(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadius = 6371 // Radio de la Tierra en kilómetros

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c // Distancia en kilómetros
}