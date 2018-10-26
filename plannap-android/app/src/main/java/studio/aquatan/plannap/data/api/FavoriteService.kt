package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.*
import studio.aquatan.plannap.data.model.Favorite
import studio.aquatan.plannap.data.model.PostFavorite

interface FavoriteService {
    @GET("/plan/favorite/")
    fun getFavoriteByPlanid(@Path("planId") planId: Long): Call<Favorite>

    @GET("/plan/favorite/")
    fun getFavoriteByUserid(@Path("userId") userId: Long): Call<Favorite>

    @POST("/plan/favs/")
    fun postFavorite(@Body favorite: PostFavorite): Call<Favorite>

    @HTTP(method = "DELETE", path = "/plan/favs/", hasBody = true)
    fun deleteFavorite(@Body favorite: PostFavorite): Call<Favorite>
}