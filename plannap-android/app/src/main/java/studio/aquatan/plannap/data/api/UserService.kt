package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.*
import studio.aquatan.plannap.data.model.User

interface UserService {
    @GET("/plan/users/{id}")
    fun getUser(@Path("id") userId: Long): Call<User>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/plan/user")
    //TODO Userを渡すのではなくnameとpasswordだけ渡す
    fun postUser(@Body user: User): Call<User>
}