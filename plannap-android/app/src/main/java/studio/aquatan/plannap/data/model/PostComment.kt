package studio.aquatan.plannap.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostComment(
    @Json(name = "text") val text: String
)