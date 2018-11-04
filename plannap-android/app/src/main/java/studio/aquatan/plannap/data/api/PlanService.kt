package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import studio.aquatan.plannap.data.model.Page
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.data.model.PostPlan

interface PlanService {

    @GET("/api/v2/plans/?limit=5")
    fun getPlans(
        @Query("cursor") key: String?,
        @Query("location") location: String? = null
    ): Call<Page<Plan>>

    @GET("/api/v2/me/plans/?limit=5")
    fun getMyPlans(
        @Query("cursor") key: String?,
        @Query("location") location: String? = null
    ): Call<Page<Plan>>

    @GET("/api/v2/me/favs/?limit=5")
    fun getMyFavPlans(@Query("cursor") key: String?): Call<Page<Plan>>

    @GET("/api/v2/plans/{id}/")
    fun getPlan(@Path("id") planId: Long): Call<Plan>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/api/v2/plans/")
    fun postPlan(@Body body: PostPlan): Call<Plan>
}