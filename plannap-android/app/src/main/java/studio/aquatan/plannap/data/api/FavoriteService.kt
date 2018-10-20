package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import studio.aquatan.plannap.data.model.Favorite

interface FavoriteService {
    @GET("/api/v1/favorites")
    fun getFavorites(): Call<List<Favorite>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/favorite")
    fun postFavorite(@Body body: HashMap<String, String>): Call<String>
}