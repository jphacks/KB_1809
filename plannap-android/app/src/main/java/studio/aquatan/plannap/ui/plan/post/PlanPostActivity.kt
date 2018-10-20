package studio.aquatan.plannap.ui.plan.post

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ActivityPlanPostBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import javax.inject.Inject

class PlanPostActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, PlanPostActivity::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPlanPostBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_plan_post)

        AndroidInjection.inject(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(PlanPostViewModel::class.java)

        binding.viewModel = viewModel
    }
}
