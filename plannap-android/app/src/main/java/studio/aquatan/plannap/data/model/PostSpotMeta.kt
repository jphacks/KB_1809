package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil

data class PostSpotMeta(
    var name: String,
    var note: String,
    val image: String,
    val lat: Float,
    val lon: Float
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