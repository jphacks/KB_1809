package studio.aquatan.plannap.data.model

import com.squareup.moshi.Json

data class Favorite(
    @Json(name = "pk") val id: Long,
    @Json(name = "user") val user: User,
    @Json(name = "plan_id") val planId: Int
)