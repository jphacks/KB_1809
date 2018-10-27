package studio.aquatan.plannap.data.model

import android.graphics.Bitmap
import androidx.recyclerview.widget.DiffUtil

data class EditableSpot (
    val id: Int,
    var name: String = "",
    var note: String = "",
    val image: Bitmap? = null,
    val lat: Double = 0.0,
    val long: Double = 0.0
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EditableSpot>() {
            override fun areItemsTheSame(oldItem: EditableSpot, newItem: EditableSpot) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: EditableSpot, newItem: EditableSpot) = oldItem == newItem
        }
    }
}