package studio.aquatan.plannap.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EditablePlan(
    @Json(name = "name") val name: String,
    @Json(name = "note") val note: String,
    @Json(name = "duration") val duration: Int,
    @Json(name = "price") val price: Int,
    @Json(name = "spots") val spotList: List<EditableSpot>
)