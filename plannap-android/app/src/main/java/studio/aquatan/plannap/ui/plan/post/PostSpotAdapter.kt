package studio.aquatan.plannap.ui.plan.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ItemSpotPostBinding

class PostSpotAdapter(
    private val layoutInflater: LayoutInflater,
    private val viewModel: PlanPostViewModel
) : ListAdapter<PostSpot, PostSpotAdapter.ViewHolder>(PostSpot.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSpotPostBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_spot_post, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemSpotPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(spot: PostSpot) {
            binding.data = spot

            spot.picture?.let {
                binding.picture.setImageBitmap(it)
            }

            binding.picture.setOnClickListener {
                viewModel.onAddPictureClick(spot.id)
            }
        }
    }
}