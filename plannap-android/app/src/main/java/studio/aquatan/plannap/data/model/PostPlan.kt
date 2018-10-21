package studio.aquatan.plannap.data.model

import androidx.recyclerview.widget.DiffUtil

data class PostPlan(
    val name: String,
    val price: Int,
    val duration: Int,
    val note: String,
    val spots: List<PostSpotMeta>
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