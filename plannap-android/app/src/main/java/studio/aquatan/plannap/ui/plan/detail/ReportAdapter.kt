package studio.aquatan.plannap.ui.plan.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.aquatan.plannap.R
import studio.aquatan.plannap.data.model.Report
import studio.aquatan.plannap.databinding.ItemReportBinding

class ReportAdapter(
    private val layoutInflater: LayoutInflater
) : ListAdapter<Report, ReportAdapter.ViewHolder>(Report.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_report, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemReportBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(report: Report) {
            binding.data = report
        }
    }
}