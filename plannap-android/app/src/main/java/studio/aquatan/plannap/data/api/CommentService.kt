package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.*
import studio.aquatan.plannap.data.model.Comment

interface CommentService {
    @GET("/api/v1/comments/")
    fun getComments(@Path("planId") planId: Long): Call<List<Comment>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/comment")
    fun postComment(@Body comment: Comment): Call<Comment>
}