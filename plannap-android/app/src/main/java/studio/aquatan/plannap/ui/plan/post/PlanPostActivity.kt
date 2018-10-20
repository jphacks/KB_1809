package studio.aquatan.plannap.ui.plan.post

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ActivityPlanPostBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import java.io.IOException
import javax.inject.Inject


class PlanPostActivity : AppCompatActivity() {

    companion object {
        private const val READ_REQUEST_CODE = 42

        fun createIntent(context: Context) = Intent(context, PlanPostActivity::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: ActivityPlanPostBinding
    private lateinit var viewModel: PlanPostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_plan_post)
        binding.setLifecycleOwner(this)

        AndroidInjection.inject(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this, viewModelFactory).get(PlanPostViewModel::class.java)
        binding.viewModel = viewModel

        val adapter = PostSpotAdapter(layoutInflater, viewModel)
        binding.recyclerView.apply {
            setAdapter(adapter)
            isNestedScrollingEnabled = false
        }

        viewModel.subscribe(adapter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = resultData?.data
            if (uri != null) {
                viewModel.onImageSelected(
                    getBitmapFromUri(uri),
                    getLatLongFromUri(uri))
            }
        }
    }

    private fun PlanPostViewModel.subscribe(adapter: PostSpotAdapter) {
        val activity = this@PlanPostActivity

        postSpotList.observe(activity, Observer { list ->
            adapter.submitList(list)
        })

        openFileChooser.observe(activity, Observer {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }

            startActivityForResult(intent, READ_REQUEST_CODE)
        })

        validation.observe(activity, Observer { result ->
            if (result.isEmptyName) {
                binding.titleLayouts.error = getString(R.string.error_require_field)
            }
            if (result.isEmptyNote) {
                binding.noteLayouts.error = getString(R.string.error_require_field)
            }
            if (result.isShortSpot) {
                Snackbar.make(binding.root, R.string.error_short_spots, Snackbar.LENGTH_LONG).show()
            }
            if (result.isInvalidSpot) {
                Snackbar.make(binding.root, R.string.error_invalid_spot, Snackbar.LENGTH_LONG).show()
            }
        })

        errorSelectedImage.observe(activity, Observer {
            Toast.makeText(activity, R.string.error_not_support_img, Toast.LENGTH_LONG).show()
        })
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r") ?: return null
            val fileDescriptor = parcelFileDescriptor.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            Log.e(javaClass.simpleName, "Failed to get Bitmap", e)
        }

        return null
    }

    private fun getLatLongFromUri(uri: Uri): FloatArray? {
        try {
            val exifInterface = ExifInterface(contentResolver.openInputStream(uri))
            val latLng = FloatArray(2)

            if (exifInterface.getLatLong(latLng)) {
                return latLng
            }
        } catch (e: IOException) {
            Log.e(javaClass.simpleName, "Failed to get LatLong", e)
        }

        return null
    }
}
