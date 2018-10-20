package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json

data class Plan(
    val id: Long,
    val note: String,
    val name: String,
    val date: String,
    val duration: Int,
    val price: Int,
    @Json(name = "favorite_count") val favoriteCount: Long,
    @Json(name = "comments_count") val commentCount: Long,
    @Json(name = "comments") val commentList: List<Comment>,
    @Json(name = "spots") val spotList: List<Spot>,
    @Json(name = "reports") val reportList: List<Report>,
    @Json(name = "favorites") val favoriteList: List<Favorite>,
    val user: User
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