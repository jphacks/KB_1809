package studio.aquatan.plannap.data.model

import android.graphics.Bitmap
import androidx.recyclerview.widget.DiffUtil

data class EditableSpot (
    val id: Int,
    var name: String = "",
    var note: String = "",
    val picture: Bitmap? = null,
    val lat: Float? = null,
    val lon: Float? = null
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EditableSpot>() {
            override fun areItemsTheSame(oldItem: EditableSpot, newItem: EditableSpot): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EditableSpot, newItem: EditableSpot): Boolean {
                return oldItem == newItem
            }
        }
    }
}