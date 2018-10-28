package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "pk") val id: Long,
    @Json(name = "p_name") val prefectureName: String,
    @Json(name = "p_code") val prefectureCode: Int,
    @Json(name = "m_name") val municipalityName: String,
    @Json(name = "m_code") val municipalityCode: Int
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