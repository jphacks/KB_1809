package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import studio.aquatan.plannap.data.CommentRepository.Companion.PAGE_SIZE
import studio.aquatan.plannap.data.model.Comment
import studio.aquatan.plannap.data.model.Page
import studio.aquatan.plannap.data.model.PostComment

interface CommentService {

    @GET("/api/v2/plans/{id}/comments/")
    fun getComments(
        @Path("id") planId: Long,
        @Query("cursor") key: String?,
        @Query("limit") limit: Int? = PAGE_SIZE
    ): Call<Page<Comment>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/v1/plans/{id}/comments/")
    fun postComment(
        @Path("id") planId: Long,
        @Body comment: PostComment
    ): Call<Comment>
}