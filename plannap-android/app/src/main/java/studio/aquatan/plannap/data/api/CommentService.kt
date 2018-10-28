package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import studio.aquatan.plannap.data.model.Comment

interface CommentService {
    @GET("/plan/comments/")
    fun getComments(@Path("planId") planId: Long): Call<List<Comment>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/plan/comment")
    fun postComment(@Body comment: Comment): Call<Comment>
}