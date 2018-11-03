package studio.aquatan.plannap.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Page<T>(
    @Json(name = "next") val nextKey: String? = null,
    @Json(name = "previous") val previousKey: String? = null,
    @Json(name = "results") val resultList: List<T> = emptyList()
)