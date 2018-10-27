package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json

data class Report(
    @Json(name = "pk")val id: Long,
    @Json(name = "plan_id") val planId: Int,
    @Json(name = "user") val user: User,
    @Json(name = "image") val imageUrl: String,
    @Json(name = "text") val text: String
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Report>() {
            override fun areItemsTheSame(oldItem: Report, newItem: Report) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Report, newItem: Report) = oldItem == newItem
        }
    }
}