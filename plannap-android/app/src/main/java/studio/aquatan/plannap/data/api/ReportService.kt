package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.*
import studio.aquatan.plannap.data.model.Report

interface ReportService {
    @GET("/api/v1/plans/{planId}/reports/")
    fun getReports(@Query("planId") planId: Int): Call<List<Report>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/plans/{planId}/reports/")
    fun postReport(@Body report: Report): Call<Report>
}