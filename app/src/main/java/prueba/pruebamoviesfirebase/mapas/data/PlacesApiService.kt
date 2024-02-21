package prueba.pruebamoviesfirebase.mapas.data

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Interfaz que define las operaciones de la API de lugares.
interface PlacesApi {
    @GET("place/nearbysearch/json")
    fun getNearbyCinemas(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") apiKey: String
    ): Call<NearbyCinemasResponse>
}


// Objeto que crea una instancia de PlacesApi utilizando Retrofit.
object PlacesClient {
    // URL base de la API de Google Places.
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    // Método estático para crear una instancia de PlacesApi.
    fun create(): PlacesApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Convierte los objetos JSON en objetos Kotlin.
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Devuelve una instancia de PlacesApi.
        return retrofit.create(PlacesApi::class.java)
    }
}