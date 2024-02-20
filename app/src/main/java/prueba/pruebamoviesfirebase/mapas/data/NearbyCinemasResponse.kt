package prueba.pruebamoviesfirebase.mapas.data

data class NearbyCinemasResponse(
    val results: List<Cinema>
)

data class Cinema(
    val name: String,
    val geometry: Geometry
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)
