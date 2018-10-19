package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.GET
import studio.aquatan.plannap.data.model.Plan

interface PlanService {
    @GET("/api/v1")
    fun plans(): Call<List<Plan>>
}