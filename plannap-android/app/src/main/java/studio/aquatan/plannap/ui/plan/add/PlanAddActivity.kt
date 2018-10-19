package studio.aquatan.plannap.ui.plan.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ActivityPlanAddBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import javax.inject.Inject

class PlanAddActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, PlanAddActivity::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPlanAddBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_plan_add)

        AndroidInjection.inject(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(PlanAddViewModel::class.java)

        binding.viewModel = viewModel
    }
}
