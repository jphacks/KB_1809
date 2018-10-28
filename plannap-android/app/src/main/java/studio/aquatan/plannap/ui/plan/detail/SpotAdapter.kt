package studio.aquatan.plannap.ui.plan.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.aquatan.plannap.R
import studio.aquatan.plannap.data.model.Spot
import studio.aquatan.plannap.databinding.ItemSpotBinding

class SpotAdapter(
    private val layoutInflater: LayoutInflater
) : ListAdapter<Spot, SpotAdapter.ViewHolder>(Spot.DIFF_CALLBACK) {

    companion object {
        private const val START_TYPE = 0 // 始点
        private const val NORMAL_TYPE = 1 // それ以外
        private const val GOAL_TYPE = 2 // 終点
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = getItem(position).id

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> START_TYPE
            position + 1 >= itemCount -> GOAL_TYPE
            else -> NORMAL_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSpotBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_spot, parent, false)

        return when (viewType) {
            START_TYPE -> StartViewHolder(binding)
            NORMAL_TYPE -> NormalViewHolder(binding)
            GOAL_TYPE -> GoalViewHolder(binding)
            else -> throw IllegalArgumentException("Invalid viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StartViewHolder(
        private val binding: ItemSpotBinding
    ) : ViewHolder(binding) {

        override fun bind(spot: Spot) {
            super.bind(spot)

            binding.topLine.visibility = View.INVISIBLE
            binding.bottomLine.visibility = View.VISIBLE
        }
    }

    class NormalViewHolder(
        private val binding: ItemSpotBinding
    ) : ViewHolder(binding) {

        override fun bind(spot: Spot) {
            super.bind(spot)

            binding.topLine.visibility = View.VISIBLE
            binding.bottomLine.visibility = View.VISIBLE
        }
    }

    class GoalViewHolder(
        private val binding: ItemSpotBinding
    ) : ViewHolder(binding) {

        override fun bind(spot: Spot) {
            super.bind(spot)

            binding.topLine.visibility = View.VISIBLE
            binding.bottomLine.visibility = View.INVISIBLE
        }
    }

    abstract class ViewHolder(
        private val binding: ItemSpotBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        open fun bind(spot: Spot) {
            binding.data = spot
        }
    }
}