package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.*
import studio.aquatan.plannap.data.model.Plan

interface PlanService {
    @GET("/api/v1/plans")
    fun getPlans(): Call<List<Plan>>

    @GET("/api/v1/plan/{id}")
    fun getPlan(@Path("id") planId: Long): Call<Plan>

    @GET("/api/v1/plan/")
    fun getPlan(@Query("word") word: String): Call<List<Plan>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/plan")
    fun postPlan(@Body plan: Plan): Call<Plan>
}