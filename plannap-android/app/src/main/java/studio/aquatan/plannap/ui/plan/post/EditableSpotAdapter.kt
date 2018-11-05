package studio.aquatan.plannap.ui.plan.post

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
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
import studio.aquatan.plannap.util.rotateImageIfRequired

class EditableSpotAdapter(
    private val layoutInflater: LayoutInflater,
    private val contentResolver: ContentResolver,
    private val onImageClick: (Int) -> Unit
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

    private fun Uri.toThumbnail(): Bitmap? {
        try {
            val parcelFile = contentResolver.openFileDescriptor(this, "r") ?: return null

            val image = BitmapFactory.decodeFileDescriptor(parcelFile.fileDescriptor)
            parcelFile.close()

            val (width, height) = image.calcScaleWidthHeight(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
            val scaledImage = Bitmap.createScaledBitmap(image, width, height, true)
            image.recycle()

            return scaledImage.rotateImageIfRequired(this, contentResolver)
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Failed to create Thumbnail", e)
        }


        return null
    }

    inner class ViewHolder(
        private val binding: ItemEditableSpotBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(spot: EditableSpot) {
            binding.data = spot

            binding.image.setOnClickListener { onImageClick(spot.id) }

            val thumbnail = spot.imageUri?.toThumbnail() ?: return

            binding.image.apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                setImageBitmap(thumbnail)
            }
        }
    }
}