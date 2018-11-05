package studio.aquatan.plannap.ui.report.post

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ActivityReportPostBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import javax.inject.Inject

class ReportPostActivity : AppCompatActivity() {

    companion object {
        private const val READ_REQUEST_CODE = 42

        private const val EXTRA_ID = "ID"
        fun createIntent(context: Context, planId: Long) =
            Intent(context,ReportPostActivity::class.java).apply {
                putExtra(EXTRA_ID, planId)
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: ActivityReportPostBinding
    private lateinit var viewModel: ReportPostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_report_post)
        binding.setLifecycleOwner(this)

        AndroidInjection.inject(this)

        viewModel = ViewModelProvider(this,viewModelFactory).get(ReportPostViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.subscribe()
    }

    override fun finish() {
        AlertDialog.Builder(this)
            .setTitle(R.string.title_confirm)
            .setMessage(R.string.text_discard_confirm)
            .setPositiveButton(R.string.action_yes) { _, _ -> super.finish() }
            .setNegativeButton(R.string.action_no, null)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = resultData?.data ?: return
            viewModel.onImageSelected(uri)
        }
    }

    private fun finishNoDialog() {
        super.finish()
    }

    private fun ReportPostViewModel.subscribe() {
        val activity = this@ReportPostActivity

        openFileChooser.observe(activity, Observer { intent ->
            startActivityForResult(intent, READ_REQUEST_CODE)
        })

        finishActivity.observe(activity, Observer { activity.finishNoDialog() })
    }
}
