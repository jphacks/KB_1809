package studio.aquatan.plannap.data.model

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EditableSpot (
    @Json(name = "id") val id: Int,
    @Json(name = "name") var name: String = "",
    @Json(name = "note") var note: String = "",
    @Json(name = "image_uri") val imageUri: Uri? = null,
    @Json(name = "lat") val lat: Double = 0.0,
    @Json(name = "long") val long: Double = 0.0,
    @Transient val isError: Boolean = false
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EditableSpot>() {
            override fun areItemsTheSame(oldItem: EditableSpot, newItem: EditableSpot) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: EditableSpot, newItem: EditableSpot) = oldItem == newItem
        }
    }
}