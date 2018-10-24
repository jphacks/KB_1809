package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import studio.aquatan.plannap.data.model.Authorization
import studio.aquatan.plannap.data.model.LoginUser

interface AuthService {

    @POST("/auth/jwt/create")
    fun login(@Body user: LoginUser): Call<Authorization>
}