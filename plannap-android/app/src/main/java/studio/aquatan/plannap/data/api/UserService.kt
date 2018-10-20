package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import studio.aquatan.plannap.data.model.User

interface UserService {
    @GET("/api/v1/users")
    fun getUsers(): Call<List<User>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/user")
    fun postUser(@Body body: HashMap<String, String>): Call<String>
}