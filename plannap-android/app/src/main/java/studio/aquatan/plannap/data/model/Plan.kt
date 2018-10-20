package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json

data class Plan(
    val id: Long,
    @Json(name = "user_id") val userId: Int,
    val name: String,
    val price: Int,
    val duration: Int,
    @Json(name = "course_id") val courseId: Int,
    val note: String,
    val spots: List<Spot>,
    val reports: List<Report>,
    val comments: List<Comment>,
    val favorites: List<Favorite>
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