package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json

data class Spot(
    val id: Long,
    @Json(name = "user_id") val userId: Int,
    val geo: Float,
    val note: String
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