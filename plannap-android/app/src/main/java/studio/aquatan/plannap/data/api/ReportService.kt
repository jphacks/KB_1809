package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import studio.aquatan.plannap.data.model.Report

interface ReportService {
    @GET("/api/v1/reports/")
    fun getReports(@Query("planId") planId: Int): Call<List<Report>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/plan/report")
    fun postReport(@Body report: Report): Call<Report>
}