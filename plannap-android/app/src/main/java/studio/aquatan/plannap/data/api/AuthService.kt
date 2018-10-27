package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import studio.aquatan.plannap.data.model.Authorization
import studio.aquatan.plannap.data.model.PostUser

interface AuthService {

    @POST("/auth/jwt/create/")
    fun login(@Body user: PostUser): Call<Authorization>

    @POST("/auth/jwt/refresh/")
    fun refresh(@Body auth: Authorization): Call<Authorization>

    @POST("/auth/jwt/verify/")
    fun verify(@Body auth: Authorization): Call<Authorization>
}