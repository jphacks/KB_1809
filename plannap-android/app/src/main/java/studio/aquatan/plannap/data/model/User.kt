package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "username") val username: String,
    @Json(name = "icon") val iconUrl: String,
    @Json(name = "email") val email: String = "unknown@example.com",
    @Json(name = "created_at") val createdDate: String,
    @Json(name = "post_plan_names") val postPlanNameList: List<String> = mutableListOf("unknown"),
    @Json(name = "post_plan_images") val postPlanImageList: List<String> = mutableListOf("unknown"),
    @Json(name = "post_plan_created_at") val postPlanCreatedDateList: List<String> = mutableListOf("unknown"),
    @Json(name = "post_plan_favorite_count") val postPlanFavoriteCountList: List<Int> = mutableListOf(0),
    @Json(name = "post_plan_comment_count") val postPlanCommentCountList: List<Int> = mutableListOf(0)
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