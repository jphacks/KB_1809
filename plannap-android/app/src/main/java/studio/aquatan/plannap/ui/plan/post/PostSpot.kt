package studio.aquatan.plannap.ui.plan.post

import android.graphics.Bitmap
import androidx.recyclerview.widget.DiffUtil

data class PostSpot (
    val id: Int,
    val picture: Bitmap? = null,
    var note: String? = null
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostSpot>() {
            override fun areItemsTheSame(oldItem: PostSpot, newItem: PostSpot): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PostSpot, newItem: PostSpot): Boolean {
                return oldItem == newItem
            }
        }
    }
}