package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import studio.aquatan.plannap.data.model.User

interface UserService {

    @GET("/api/v2/me/")
    fun getUser(): Call<User>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/users/")
    //TODO Userを渡すのではなくnameとpasswordだけ渡す
    fun postUser(@Body user: User): Call<User>
}