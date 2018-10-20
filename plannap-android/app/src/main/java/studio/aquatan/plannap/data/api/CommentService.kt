package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import studio.aquatan.plannap.data.model.Comment

interface CommentService {
    @GET("/api/v1/comments")
    fun getComments(): Call<List<Comment>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/comment")
    fun postComment(@Body body: HashMap<String, String>): Call<String>
}