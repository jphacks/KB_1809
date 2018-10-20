package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import studio.aquatan.plannap.data.model.Plan

interface UserService {
    @GET("/api/v1/piyo")
    fun plans(): Call<List<Plan>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/hoge")
    fun postPicture(@Body body: HashMap<String, String>): Call<String>
}