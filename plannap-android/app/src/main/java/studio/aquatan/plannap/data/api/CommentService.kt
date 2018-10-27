package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.*
import studio.aquatan.plannap.data.model.Comment
import studio.aquatan.plannap.data.model.PostComment

interface CommentService {
    @GET("/plan/comments/")
    fun getComments(@Path("planId") planId: Long): Call<List<Comment>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/plan/plans/{id}/comments/")
    fun postComment(
        @Path("id") planId: Long,
        @Body comment: PostComment
    ): Call<Comment>
}