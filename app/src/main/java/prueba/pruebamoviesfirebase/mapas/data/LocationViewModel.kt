package prueba.pruebamoviesfirebase.mapas.data

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.cos

class LocationViewModel : ViewModel() {
    private val userLocation = mutableStateOf<LatLng?>(null)
    private val isLocationPermissionGranted = mutableStateOf(false)
    private val _cinemas = MutableStateFlow<List<Cinema>>(emptyList())
    val cinemas: StateFlow<List<Cinema>> = _cinemas

    fun searchPlaces(placesClient: PlacesClient): List<Place> {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME)
        val radiusMeters = 1000.0 // Radio de búsqueda en metros
        val bounds = calculateBoundsAroundUserLocation(userLocation.value, radiusMeters)
        val searchByTextRequest = SearchByTextRequest.builder("Cinemas", placeFields)
            .setMaxResultCount(5)
            .setLocationRestriction(bounds)
            .build()
        var places: List<Place> = listOf()
        placesClient.searchByText(searchByTextRequest)
            .addOnSuccessListener { response ->
                places = response.places
            }
        return places
    }





    @Composable
    fun getUserLocation(context: Context) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val requestPermissionLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location ->
                            if (location != null) {
                                userLocation.value = LatLng(location.latitude, location.longitude)
                                isLocationPermissionGranted.value = true
                            }
                        }
                } else {
                    // Handle case where permission is not granted
                    isLocationPermissionGranted.value = false
                }
            }

        if (ContextCompat.checkSelfPermission(
                context,
                ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        userLocation.value = LatLng(location.latitude, location.longitude)
                    }
                }
        } else {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    private fun calculateBoundsAroundUserLocation(userLocation: LatLng?, radiusMeters: Double): RectangularBounds {
        // Calcula la distancia en grados de longitud correspondiente a "radiusMeters" metros
        userLocation.let {
            val longitudeDelta = radiusMeters / (111320 * cos((userLocation?.latitude ?: 1.0) * Math.PI / 180))
            // Calcula la distancia en grados de latitud correspondiente a "radiusMeters" metros
            val latitudeDelta = radiusMeters / 110574

            // Calcula las coordenadas de la esquina suroeste del rectángulo
            val southWest = LatLng(
                userLocation?.latitude?.minus(latitudeDelta) ?: 0.0,
                userLocation?.longitude?.minus(longitudeDelta) ?: 0.0
            )

            // Calcula las coordenadas de la esquina noreste del rectángulo
            val northEast = LatLng(
                userLocation?.latitude?.plus(latitudeDelta) ?: 0.0,
                userLocation?.longitude?.plus(longitudeDelta) ?: 0.0
            )
            return RectangularBounds.newInstance(southWest, northEast)
        }
        // Crea y devuelve el objeto RectangularBounds con las coordenadas calculadas
    }
}
