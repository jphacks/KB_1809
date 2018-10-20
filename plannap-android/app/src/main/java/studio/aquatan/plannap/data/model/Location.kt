package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json

data class Location(
    val id: Long,
    @Json(name = "pname") val prefectureName: String,
    @Json(name = "pcode") val prefectureCode: Int,
    @Json(name = "mname") val municipalityName: String,
    @Json(name = "mcode") val municipality: String
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