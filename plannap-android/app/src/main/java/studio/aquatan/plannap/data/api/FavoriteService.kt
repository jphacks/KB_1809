package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.*
import studio.aquatan.plannap.data.model.Favorite

interface FavoriteService {
    @GET("/plan/favorite/")
    fun getFavoriteByPlanid(@Path("planId") planId: Long): Call<Favorite>

    @GET("/plan/favorite/")
    fun getFavoriteByUserid(@Path("userId") userId: Long): Call<Favorite>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/plan/favorite")
    fun postFavorite(@Body favorite: Favorite): Call<Favorite>
}