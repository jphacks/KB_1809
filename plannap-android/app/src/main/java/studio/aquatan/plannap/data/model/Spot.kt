package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Spot(
    @Json(name = "pk") val id: Long,
    @Json(name = "plan_id") val planId: Long,
    @Json(name = "name") val name: String,
    @Json(name = "order") val order: Int,
    @Json(name = "lat") val lat: Float,
    @Json(name = "lon") val lon: Float,
    @Json(name = "note") val note: String,
    @Json(name = "image") val imageUrl: String,
    @Json(name = "created_at") val createdDate: String
) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Spot>() {
            override fun areItemsTheSame(oldItem: Spot, newItem: Spot) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Spot, newItem: Spot) = oldItem == newItem
        }
    }
}