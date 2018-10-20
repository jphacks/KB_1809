package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import studio.aquatan.plannap.data.model.Location

interface LocationService {
    @GET("/api/v1/locations")
    fun getLocations(): Call<List<Location>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/location")
    fun postLocation(@Body body: HashMap<String, String>): Call<String>
}