package studio.aquatan.plannap.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ActivityLoginBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import studio.aquatan.plannap.ui.main.MainActivity
import studio.aquatan.plannap.util.hideSoftInput
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.setLifecycleOwner(this)

        AndroidInjection.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.subscribe()
    }

    private fun LoginViewModel.subscribe() {
        val activity = this@LoginActivity

        validation.observe(activity, Observer { result ->
            if (result.isEmptyUsername) {
                binding.usernameLayout.error = getString(R.string.error_require_field)
            }
            if (result.isEmptyPassword) {
                binding.passwordLayout.error = getString(R.string.error_require_field)
            }
        })

        hideSoftInput.observe(activity, Observer { activity.hideSoftInput() })

        startMainActivity.observe(activity, Observer {
            startActivity(MainActivity.createIntent(activity))
            finish()
        })
    }
}
