package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import studio.aquatan.plannap.data.model.Report

interface ReportService {
    @GET("/api/v1/reports")
    fun getReports(): Call<List<Report>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/report")
    fun postReport(@Body body: HashMap<String, String>): Call<String>
}