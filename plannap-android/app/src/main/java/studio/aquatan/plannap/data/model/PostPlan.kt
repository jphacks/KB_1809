package studio.aquatan.plannap.data.model

import com.squareup.moshi.Json


data class PostPlan(
    @Json(name = "name") val name: String,
    @Json(name = "price") val price: Int?,
    @Json(name = "duration") val duration: Int?,
    @Json(name = "note") val note: String,
    @Json(name = "spots") val spotList: List<PostSpot>
)