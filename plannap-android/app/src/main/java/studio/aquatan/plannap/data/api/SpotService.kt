package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import studio.aquatan.plannap.data.model.Spot

interface SpotService {
    @GET("/api/v1/spots")
    fun getSpots(): Call<List<Spot>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/spot")
    fun postSpot(@Body body: HashMap<String, String>): Call<String>
}