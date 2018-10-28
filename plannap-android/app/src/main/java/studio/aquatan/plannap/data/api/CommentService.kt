package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import studio.aquatan.plannap.data.model.Comment
import studio.aquatan.plannap.data.model.PostComment

interface CommentService {
    @GET("/api/v1/plans/{id}/comments/")
    fun getCommentsByPlanId(@Path("id") planId: Long): Call<List<Comment>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/plans/{id}/comments/")
    fun postComment(
        @Path("id") planId: Long,
        @Body comment: PostComment
    ): Call<Comment>
}