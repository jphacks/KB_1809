package studio.aquatan.plannap.ui.plan.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.aquatan.plannap.data.model.Spot

class SpotAdapter(
    private val layoutInflater: LayoutInflater
) : ListAdapter<Spot, SpotAdapter.ViewHolder>(Spot.DIFF_CALLBACK) {

    companion object {
        private const val START_TYPE = 0 // 始点
        private const val NORMAL_TYPE = 1 // それ以外
        private const val END_TYPE = 2 // 終点
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = getItem(position).id

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> START_TYPE
            position + 1 >= itemCount -> END_TYPE
            else -> NORMAL_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            START_TYPE -> {}
            NORMAL_TYPE -> {}
            END_TYPE -> {}
        }
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}