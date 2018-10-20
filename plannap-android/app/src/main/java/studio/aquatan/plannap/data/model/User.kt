package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json

data class User(
    @Json(name = "pk") val id: Long,
    val username: String,
    val email: String = "unknown",
    @Json(name = "created_at") val createdDate: String = "unknown",
    @Json(name = "icon") val iconUrl: String,
    @Json(name = "update_at") val updateDate: String = "unknown"
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Plan>() {
            override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
                return oldItem == newItem
            }
        }
    }
}