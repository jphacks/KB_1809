package studio.aquatan.plannap.ui.plan

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.aquatan.plannap.R
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.databinding.ItemPlanBinding

class PlanAdapter(
    private val layoutInflater: LayoutInflater,
    private val onClick: (Long) -> Unit,
    private val onFavoriteClick: (Long, Boolean) -> Unit
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
            binding.apply {
                data = plan
                root.setOnClickListener { onClick(plan.id) }

                planImage.setImageURI(plan.spotList.first().imageUrl)

                startSpotName.text = plan.spotList.first().name
                goalSpotName.text = plan.spotList.last().name

                duration.text = plan.duration.toString() + "分"

                commentButton.text = plan.commentCount.toString()

                favoriteButton.apply {
                    count = plan.favoriteCount
                    setOnFavoriteChangedListener {
                        plan.isFavorite = it
                        onFavoriteClick(plan.id, it)
                    }
                }
            }
        }
    }
}