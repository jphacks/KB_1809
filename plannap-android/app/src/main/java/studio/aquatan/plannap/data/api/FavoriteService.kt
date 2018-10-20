package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.*
import studio.aquatan.plannap.data.model.Favorite

interface FavoriteService {
    @GET("/api/v1/favorite/")
    fun getFavoriteByPlanid(@Path("planId") planId: Long): Call<Favorite>

    @GET("/api/v1/favorite/")
    fun getFavoriteByUserid(@Path("userId") userId: Long): Call<Favorite>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/favorite")
    fun postFavorite(@Body favorite: Favorite): Call<Favorite>
}