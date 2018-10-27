package studio.aquatan.plannap.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostSpot(
    @Json(name = "name") val name: String,
    @Json(name = "note") val note: String,
    @Json(name = "image") val image: String,
    @Json(name = "lat") val lat: Float,
    @Json(name = "lon") val lon: Float
)