package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import studio.aquatan.plannap.data.model.User

interface UserService {
    @GET("/plan/users/{id}")
    fun getUser(@Path("id") userId: Long): Call<User>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/plan/user")
    //TODO Userを渡すのではなくnameとpasswordだけ渡す
    fun postUser(@Body user: User): Call<User>
}