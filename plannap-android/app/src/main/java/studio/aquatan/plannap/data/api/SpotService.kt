package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.*
import studio.aquatan.plannap.data.model.Spot

interface SpotService {
    @GET("/plan/spots/")
    fun getSpots(@Query("planId") planId: Int): Call<List<Spot>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/plan/spot")
    fun postSpot(@Body spot: Spot): Call<Spot>
}