package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import studio.aquatan.plannap.data.model.PostReport
import studio.aquatan.plannap.data.model.Report

interface ReportService {
    @GET("/api/v1/plans/{id}/reports/")
    fun getReports(@Query("id") planId: Int): Call<List<Report>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/plans/{plan_id}/reports/")
    fun postReport(@Query("plan_id") planId: Int,@Body report: PostReport): Call<Report>
}