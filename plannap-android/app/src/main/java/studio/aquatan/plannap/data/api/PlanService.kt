package studio.aquatan.plannap.data.api

import retrofit2.Call
import retrofit2.http.*
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.data.model.PostPlan

interface PlanService {
    //現在，トークンはハードコーディング
    @Headers("Authorization: JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJ1c2VybmFtZSI6ImRldmVsb3AiLCJleHAiOjE1NDAzMDY5MTIsImVtYWlsIjoiaG9nZUBnbWFpbC5jb20iLCJvcmlnX2lhdCI6MTU0MDA0NzcxMn0.67rGu3zwo88AQutMdEpdMlWokn6j_ymGrf1eX-t0Y1A")
    @GET("/plan/plans/")
    fun getPlans(): Call<List<Plan>>

    @Headers("Authorization: JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJ1c2VybmFtZSI6ImRldmVsb3AiLCJleHAiOjE1NDAzMDY5MTIsImVtYWlsIjoiaG9nZUBnbWFpbC5jb20iLCJvcmlnX2lhdCI6MTU0MDA0NzcxMn0.67rGu3zwo88AQutMdEpdMlWokn6j_ymGrf1eX-t0Y1A")
    @GET("/plan/plans/{id}")
    fun getPlan(@Path("id") planId: Long): Call<Plan>

    @Headers("Authorization: JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJ1c2VybmFtZSI6ImRldmVsb3AiLCJleHAiOjE1NDAzMDY5MTIsImVtYWlsIjoiaG9nZUBnbWFpbC5jb20iLCJvcmlnX2lhdCI6MTU0MDA0NzcxMn0.67rGu3zwo88AQutMdEpdMlWokn6j_ymGrf1eX-t0Y1A")
    @GET("/plan/plans/")
    fun getPlan(@Query("location") location: String): Call<List<Plan>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Authorization: JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJ1c2VybmFtZSI6ImRldmVsb3AiLCJleHAiOjE1NDA1NjY2MjEsImVtYWlsIjoiaG9nZUBnbWFpbC5jb20iLCJvcmlnX2lhdCI6MTU0MDMwNzQyMX0.Af_dX3BzQzDSPxLvHtYxLY5UjZf4HfhSfuiC7yy5fwQ"
    )
    @POST("/plan/plans/")
    fun postPlan(@Body body: PostPlan): Call<String>
}