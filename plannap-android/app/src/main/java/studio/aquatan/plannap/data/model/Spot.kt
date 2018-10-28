package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json

data class Spot(
    @Json(name = "pk") val id: Long,
    val name: String,
    val order: Int,
    val lat: Float,
    val lon: Float,
    val note: String,
    @Json(name = "image") val imageUrl: String,
    @Json(name = "created_at") val createdDate: String
) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Spot>() {
            override fun areItemsTheSame(oldItem: Spot, newItem: Spot): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Spot, newItem: Spot): Boolean {
                return oldItem == newItem
            }
        }
    }
}