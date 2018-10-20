package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import studio.aquatan.plannap.data.model.Location

interface LocationService {

    @GET("/api/v1/location/{id}")
    fun getLocation(@Path("id") locationId: Long): Call<Location>

}