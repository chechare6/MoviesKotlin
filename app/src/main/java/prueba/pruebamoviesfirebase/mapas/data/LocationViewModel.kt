import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import prueba.pruebamoviesfirebase.mapas.data.Cinema
import prueba.pruebamoviesfirebase.mapas.data.Geometry
import prueba.pruebamoviesfirebase.mapas.data.Location

class LocationViewModel : ViewModel() {
    val userLocation = mutableStateOf<LatLng?>(null)
    val isLocationPermissionGranted = mutableStateOf(false)
    private val _cinemas = MutableStateFlow<List<Cinema>>(emptyList())
    val cinemas: StateFlow<List<Cinema>> = _cinemas

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

    fun nearbySearch(context: Context) {
        val userLocation = userLocation.value

        if (userLocation != null) {
            // Initialize Places if not initialized
            if (!Places.isInitialized()) {
                Places.initialize(context, "YOUR_API_KEY")
            }

            // Create Places client
            val placesClient = Places.createClient(context)

            // Define fields to retrieve from nearby places
            val placeFields = listOf(Place.Field.NAME, Place.Field.LAT_LNG)

            // Create nearby search request (for cinemas in this case)
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            // Perform nearby places search
            val nearbyCinemas = mutableListOf<Cinema>()
            placesClient.findCurrentPlace(request).addOnSuccessListener { response: FindCurrentPlaceResponse ->
                for (placeLikelihood in response.placeLikelihoods) {
                    val place = placeLikelihood.place
                    nearbyCinemas.add(
                        Cinema(
                            name = place.name ?: "",
                            geometry = Geometry(
                                location = Location(
                                    lat = place.latLng?.latitude ?: 0.0,
                                    lng = place.latLng?.longitude ?: 0.0
                                )
                            )
                        )
                    )
                }
                _cinemas.value = nearbyCinemas
            }.addOnFailureListener { exception: Exception ->
                // Handle search failures
                exception.printStackTrace()
            }
        } else {
            // Handle case where user location is not available
            _cinemas.value = emptyList()
        }
    }
}
