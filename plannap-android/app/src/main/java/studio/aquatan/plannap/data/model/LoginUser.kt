package studio.aquatan.plannap.data.model

import com.squareup.moshi.Json

data class LoginUser(
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String
)