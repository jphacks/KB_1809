package studio.aquatan.plannap.ui.plan.post

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import studio.aquatan.plannap.util.hideSoftInput
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

        val adapter = EditableSpotAdapter(layoutInflater, viewModel)
        binding.recyclerView.apply {
            setAdapter(adapter)
            isNestedScrollingEnabled = false
        }

        viewModel.subscribe(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_post -> {
                hideSoftInput()
                viewModel.onPostClick()
            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = resultData?.data ?: return
            viewModel.onImageSelected(uri)
        }
    }

    private fun PlanPostViewModel.subscribe(adapter: EditableSpotAdapter) {
        val activity = this@PlanPostActivity

        spotList.observe(activity, Observer {
            adapter.submitList(it.toList())
        })

        openFileChooser.observe(activity, Observer {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }

            startActivityForResult(intent, READ_REQUEST_CODE)
        })

        finishActivity.observe(activity, Observer {
            Toast.makeText(activity, R.string.text_post_success, Toast.LENGTH_LONG).show()
            activity.finish()
        })

        validation.observe(activity, Observer { result ->
            if (result.isEmptyName) {
                binding.nameLayout.error = getString(R.string.error_require_field)
            }
            if (result.isEmptyNote) {
                binding.noteLayout.error = getString(R.string.error_require_field)
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
}
