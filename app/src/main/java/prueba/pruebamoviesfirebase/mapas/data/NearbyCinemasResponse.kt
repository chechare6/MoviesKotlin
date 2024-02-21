package prueba.pruebamoviesfirebase.mapas.data

// Clase de datos que representa la respuesta de cines cercanos
data class NearbyCinemasResponse(
    val results: List<Cinema>
)

// Clase de datos que representa un cine
data class Cinema(
    val name: String,
    val geometry: Geometry
)

// Clase de datos que representa la geometría del cine
data class Geometry(
    val location: Location
)

// Clase de datos que representa la ubicación (latitud y longitud)
data class Location(
    val lat: Double,
    val lng: Double
)
