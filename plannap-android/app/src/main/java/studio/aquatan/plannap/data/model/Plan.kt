package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json

data class Plan(
    @Json(name = "pk") val id: Long,
    val name: String,
    val price: Int,
    val duration: Int,
    val note: String,
    val location: Location,
    @Json(name = "reports") val reportList: List<Report> = emptyList(),
    @Json(name = "favorites") val favoriteList: List<Favorite> = emptyList(),
    @Json(name = "created_at") val date: String,
    @Json(name = "comments") val commentList: List<Comment> = emptyList(),
    @Json(name = "spots") val spotList: List<Spot>,
    val user: User,
    @Json(name = "favorite_count") val favoriteCount: Int,
    @Json(name = "comment_count") val commentCount: Int
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