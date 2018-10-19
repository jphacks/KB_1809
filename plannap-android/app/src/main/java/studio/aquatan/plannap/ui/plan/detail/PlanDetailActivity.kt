package studio.aquatan.plannap.ui.plan.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ActivityPlanDetailBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import javax.inject.Inject

class PlanDetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_ID = "ID"

        fun createIntent(context: Context, id: Long) =
            Intent(context, PlanDetailActivity::class.java).apply {
                putExtra(EXTRA_ID, id)
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: ActivityPlanDetailBinding
    private lateinit var viewModel: PlanDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_plan_detail)
        binding.setLifecycleOwner(this)

        AndroidInjection.inject(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this, viewModelFactory).get(PlanDetailViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.onActivityCreated(intent.getLongExtra(EXTRA_ID, -1))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
