package studio.aquatan.plannap.ui.plan.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ActivityPlanSearchBinding
import studio.aquatan.plannap.ui.plan.searchresult.PlanSearchResultActivity

class PlanSearchActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, PlanSearchActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPlanSearchBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_plan_search)
        binding.setLifecycleOwner(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewModel = ViewModelProviders.of(this).get(PlanSearchViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.subscribe()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun PlanSearchViewModel.subscribe() {
        val activity = this@PlanSearchActivity

        startSearchResultActivity.observe(activity, Observer { areaName ->
            startActivity(PlanSearchResultActivity.createIntent(activity, areaName))
        })
    }
}
