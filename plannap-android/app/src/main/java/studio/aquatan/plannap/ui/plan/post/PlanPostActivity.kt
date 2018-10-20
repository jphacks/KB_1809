package studio.aquatan.plannap.ui.plan.post

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ActivityPlanPostBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import javax.inject.Inject


class PlanPostActivity : AppCompatActivity() {

    companion object {
        private const val READ_REQUEST_CODE = 42

        fun createIntent(context: Context) = Intent(context, PlanPostActivity::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: PlanPostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPlanPostBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_plan_post)

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
                viewModel.onImageSelected(getBitmapFromUri(uri))
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
    }

    @Throws(Exception::class)
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r") ?: throw Exception()
        val fileDescriptor = parcelFileDescriptor.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }
}
