package studio.aquatan.plannap.ui.plan.searchresult

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.android.AndroidInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ActivityPlanSearchResultBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import studio.aquatan.plannap.ui.plan.PlanAdapter
import studio.aquatan.plannap.ui.plan.detail.PlanDetailActivity
import javax.inject.Inject

class PlanSearchResultActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_AREA_NAME = "AREA_NAME"

        fun createIntent(context: Context, areaName: String) =
            Intent(context, PlanSearchResultActivity::class.java).apply {
                putExtra(EXTRA_AREA_NAME, areaName)
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: ActivityPlanSearchResultBinding
    private lateinit var viewModel: PlanSearchResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_plan_search_result)
        binding.setLifecycleOwner(this)

        AndroidInjection.inject(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this, viewModelFactory).get(PlanSearchResultViewModel::class.java)
        binding.viewModel = viewModel

        val adapter = PlanAdapter(layoutInflater, viewModel::onPlanClick)

        binding.recyclerView.apply {
            setAdapter(adapter)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        viewModel.subscribe(adapter)
        viewModel.onActivityCreated(intent.getStringExtra(EXTRA_AREA_NAME))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun PlanSearchResultViewModel.subscribe(adapter: PlanAdapter) {
        val activity = this@PlanSearchResultActivity

        title.observe(activity, Observer { activity.title = it })

        planList.observe(activity, Observer { list ->
            binding.progressBar.isVisible = false
            binding.notFoundText.isVisible = list.isEmpty()

            adapter.submitList(list)
        })

        startPlanDetailActivity.observe(activity, Observer { id ->
            startActivity(PlanDetailActivity.createIntent(activity, id))
        })
    }
}
