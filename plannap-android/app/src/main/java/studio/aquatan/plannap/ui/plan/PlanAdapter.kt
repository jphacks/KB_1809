package studio.aquatan.plannap.ui.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.aquatan.plannap.data.NetworkState
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.databinding.ItemNetworkStateBinding
import studio.aquatan.plannap.databinding.ItemPlanBinding
import studio.aquatan.plannap.databinding.ItemPlanSmallBinding

class PlanAdapter(
    private val layoutInflater: LayoutInflater,
    private val onClick: (Long) -> Unit,
    private val onRetryClick: () -> Unit,
    private val isSmallLayout: Boolean = false
) : PagedListAdapter<Plan, RecyclerView.ViewHolder>(Plan.DIFF_CALLBACK) {

    companion object {
        private const val PLAN_VIEW = 0
        private const val PLAN_SMALL_VIEW = 1
        private const val NETWORK_STATE_VIEW = 2
    }

    private var networkState: NetworkState? = null

    override fun getItemViewType(position: Int): Int {
        return when {
            hasExtraRow() && position >= itemCount - 1 -> NETWORK_STATE_VIEW
            isSmallLayout -> PLAN_SMALL_VIEW
            else -> PLAN_VIEW
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PLAN_VIEW -> PlanViewHolder(
                ItemPlanBinding.inflate(layoutInflater, parent, false)
            )
            PLAN_SMALL_VIEW -> PlanSmallViewHolder(
                ItemPlanSmallBinding.inflate(layoutInflater, parent, false)
            )
            NETWORK_STATE_VIEW -> NetworkStateViewHolder(
                ItemNetworkStateBinding.inflate(layoutInflater, parent, false)
            )
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PlanViewHolder -> holder.bind(getItem(position) ?: return)
            is PlanSmallViewHolder -> holder.bind(getItem(position) ?: return)
            is NetworkStateViewHolder -> holder.bind(networkState)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = networkState
        val hadExtraRow = hasExtraRow()
        networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    inner class PlanViewHolder(
        private val binding: ItemPlanBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: Plan) {
            binding.apply {
                data = plan
                root.setOnClickListener { onClick(plan.id) }

                planImage.setImageURI(plan.spotList.first().imageUrl)

                startSpotName.text = plan.spotList.first().name
                goalSpotName.text = plan.spotList.last().name
            }
        }
    }

    inner class PlanSmallViewHolder(
        private val binding: ItemPlanSmallBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: Plan) {
            binding.apply {
                data = plan
                root.setOnClickListener { onClick(plan.id) }

                planImage.setImageURI(plan.spotList.first().imageUrl)
            }
        }
    }

    inner class NetworkStateViewHolder(
        private val binding: ItemNetworkStateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(state: NetworkState?) {
            binding.data = state
            binding.retryButton.setOnClickListener { onRetryClick() }
        }
    }
}