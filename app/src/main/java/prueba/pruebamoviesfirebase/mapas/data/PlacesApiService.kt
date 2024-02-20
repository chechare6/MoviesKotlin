package prueba.pruebamoviesfirebase.mapas.data

import android.util.Log
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {
    @GET("place/nearbysearch/json")
    fun getNearbyCinemas(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") apiKey: String
    ): Call<NearbyCinemasResponse>
}


//BUSCAR SERVICE EN CODIGO DE DANI ES UN PROBLEMA CON ESTE BUILDER 90%
object PlacesClient {
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    fun create(): PlacesApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        Log.e("CINE", "url: ${retrofit.baseUrl().toString()}")
        return retrofit.create(PlacesApi::class.java)
    }
}