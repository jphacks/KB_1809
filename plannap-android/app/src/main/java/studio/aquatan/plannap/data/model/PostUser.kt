package studio.aquatan.plannap.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostUser(
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String
)