package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
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