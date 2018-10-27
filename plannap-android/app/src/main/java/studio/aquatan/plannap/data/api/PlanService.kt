package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.*
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.data.model.PostPlan

interface PlanService {

    @GET("/api/v1//plans/")
    fun getPlans(): Call<List<Plan>>

    @GET("/api/v1/plans/{id}/")
    fun getPlan(@Path("id") planId: Long): Call<Plan>

    @GET("/api/v1//plans/")
    fun getPlan(@Query("location") location: String): Call<List<Plan>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/api/v1//plans/")
    fun postPlan(@Body body: PostPlan): Call<Plan>
}