package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.*
import studio.aquatan.plannap.data.model.Favorite

interface FavoriteService {
    @GET("/plan/favorite/")
    fun getFavoriteByPlanid(@Path("planId") planId: Long): Call<Favorite>

    @GET("/plan/favorite/")
    fun getFavoriteByUserid(@Path("userId") userId: Long): Call<Favorite>

    @POST("/plan/plans/{id}/favs/me/")
    fun postFavorite(@Path("id") planId: Long): Call<Favorite>

    @DELETE("/plan/plans/{id}/favs/me/")
    fun deleteFavorite(@Path("id") planId: Long): Call<Favorite>
}