package studio.aquatan.plannap.data.model

import com.squareup.moshi.Json

data class Authorization(
    @Json(name = "token") val token: String
)