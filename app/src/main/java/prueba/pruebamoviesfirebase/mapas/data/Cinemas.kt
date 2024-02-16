package prueba.pruebamoviesfirebase.mapas.data

data class Cinema(
    val geometry: Geometry,
    val name: String
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Cinemas(
    val cines: List<Cinema>
)
