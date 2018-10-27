package studio.aquatan.plannap.ui.plan.post

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.aquatan.plannap.R
import studio.aquatan.plannap.data.model.EditableSpot
import studio.aquatan.plannap.databinding.ItemEditableSpotBinding
import studio.aquatan.plannap.util.calcScaleWidthHeight

class EditableSpotAdapter(
    private val layoutInflater: LayoutInflater,
    private val viewModel: PlanPostViewModel
) : ListAdapter<EditableSpot, EditableSpotAdapter.ViewHolder>(EditableSpot.DIFF_CALLBACK) {

    companion object {
        private const val THUMBNAIL_SIZE = 150.0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemEditableSpotBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_editable_spot, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemEditableSpotBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(spot: EditableSpot) {
            binding.data = spot

            binding.image.setOnClickListener {
                viewModel.onAddPictureClick(spot.id)
            }

            val image = spot.image ?: return
            val (width, height) = image.calcScaleWidthHeight(THUMBNAIL_SIZE, THUMBNAIL_SIZE)

            val thumbnail =  Bitmap.createScaledBitmap(image, width, height, true)

            binding.image.apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                setImageBitmap(thumbnail)
            }
        }
    }
}