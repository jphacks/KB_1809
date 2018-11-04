package studio.aquatan.plannap.ui.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.aquatan.plannap.data.NetworkState
import studio.aquatan.plannap.data.model.Comment
import studio.aquatan.plannap.databinding.ItemCommentBinding
import studio.aquatan.plannap.databinding.ItemNetworkStateBinding

class CommentAdapter(
    private val layoutInflater: LayoutInflater,
    private val onRetryClick: () -> Unit
) : PagedListAdapter<Comment, RecyclerView.ViewHolder>(Comment.DIFF_CALLBACK) {

    companion object {
        private const val COMMENT_VIEW = 0
        private const val NETWORK_STATE_VIEW = 1
    }

    private var networkState: NetworkState? = null

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position >= itemCount - 1) NETWORK_STATE_VIEW else COMMENT_VIEW
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            COMMENT_VIEW -> CommentViewHolder(
                ItemCommentBinding.inflate(layoutInflater, parent, false)
            )
            NETWORK_STATE_VIEW -> NetworkStateViewHolder(
                ItemNetworkStateBinding.inflate(layoutInflater, parent, false)
            )
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CommentViewHolder -> holder.bind(getItem(position) ?: return)
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

    inner class CommentViewHolder(
        private val binding: ItemCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            binding.data = comment
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