package studio.aquatan.plannap.data.model

import com.squareup.moshi.Json

data class PostFavorite(
    @Json(name = "plan_id") val planId: Long
)