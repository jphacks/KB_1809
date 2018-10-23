package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json

data class Plan(
    @Json(name = "pk") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "note") val note: String,
    @Json(name = "price") val price: Int,
    @Json(name = "duration") val duration: Int,
    @Json(name = "created_at") val date: String,
    @Json(name = "location") val location: Location?,
    @Json(name = "favorite_count") val favoriteCount: Int,
    @Json(name = "comment_count") val commentCount: Int,
    @Json(name = "spots") val spotList: List<Spot> = emptyList(),
    @Json(name = "favorites") val favoriteList: List<Favorite> = emptyList(),
    @Json(name = "reports") val reportList: List<Report> = emptyList(),
    @Json(name = "comments") val commentList: List<Comment> = emptyList(),
    @Json(name = "user") val user: User,
    @Json(name = "is_favorite") val isFavorite: Boolean
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Plan>() {
            override fun areItemsTheSame(oldItem: Plan, newItem: Plan) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Plan, newItem: Plan) = oldItem == newItem
        }
    }
}