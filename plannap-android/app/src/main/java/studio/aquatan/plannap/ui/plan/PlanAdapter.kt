package studio.aquatan.plannap.ui.plan

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import studio.aquatan.plannap.R
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.databinding.ItemPlanBinding

class PlanAdapter(
    private val layoutInflater: LayoutInflater,
    private val onClick: (Long) -> Unit
) : ListAdapter<Plan, PlanAdapter.ViewHolder>(Plan.DIFF_CALLBACK) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = getItem(position).id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPlanBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_plan, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemPlanBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("CheckResult")
        fun bind(plan: Plan) {
            binding.data = plan
            binding.root.setOnClickListener { onClick(plan.id) }

            // User
            binding.user.text = plan.user.username
            Glide.with(binding.root)
                .load(plan.user.iconUrl)
                .into(binding.userImage)

            Glide.with(binding.root)
                .load(plan.spotList.first().imageUrl)
                .into(binding.picture)

            binding.apply {
                data = plan
                root.setOnClickListener { onClick(plan.id) }

                start.text = plan.spotList.first().name
                goal.text = plan.spotList.last().name

                time.text = plan.duration.toString()

                favoriteButton.text = plan.favoriteCount.toString()
                commentsButton.text = plan.commentCount.toString()
            }
        }
    }
}