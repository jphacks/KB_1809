package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import studio.aquatan.plannap.data.model.Favorite

interface FavoriteService {
    @GET("/api/v1/plans/{id}/favs/")
    fun getFavoriteByPlanId(@Path("id") planId: Long): Call<Favorite>

    @GET("/api/v1/favorite/")
    fun getFavoriteByUserId(@Path("userId") userId: Long): Call<Favorite>

    @POST("/api/v1/plans/{id}/favs/me/")
    fun postFavorite(@Path("id") planId: Long): Call<Favorite>

    @DELETE("/api/v1/plans/{id}/favs/me/")
    fun deleteFavorite(@Path("id") planId: Long): Call<Favorite>
}