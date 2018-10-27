package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.*
import studio.aquatan.plannap.data.model.Favorite

interface FavoriteService {
    @GET("/api/v1/plans/{id}/favs/")
    fun getFavoriteByPlanid(@Path("id") planId: Long): Call<Favorite>

    @GET("/api/v1/favorite/")
    fun getFavoriteByUserid(@Path("userId") userId: Long): Call<Favorite>

    @POST("/api/v1/plans/{id}/favs/me/")
    fun postFavorite(@Path("id") planId: Long): Call<Favorite>

    @DELETE("/api/v1/plans/{id}/favs/me/")
    fun deleteFavorite(@Path("id") planId: Long): Call<Favorite>
}