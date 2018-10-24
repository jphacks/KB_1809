package studio.aquatan.plannap.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ActivityMainBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import studio.aquatan.plannap.ui.favorite.FavoriteFragment
import studio.aquatan.plannap.ui.home.HomeFragment
import studio.aquatan.plannap.ui.login.LoginActivity
import studio.aquatan.plannap.ui.plan.post.PlanPostActivity
import studio.aquatan.plannap.ui.plan.search.PlanSearchFragment
import studio.aquatan.plannap.ui.profile.ProfileFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private val fragmentMap by lazy(LazyThreadSafetyMode.NONE) {
        mapOf(
            MainFragmentType.HOME to HomeFragment.newInstance(),
            MainFragmentType.SEARCH to PlanSearchFragment.newInstance(),
            MainFragmentType.FAVORITE to FavoriteFragment.newInstance(),
            MainFragmentType.PROFILE to ProfileFragment.newInstance()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.setLifecycleOwner(this)
        setSupportActionBar(binding.appBar.toolbar)

        AndroidInjection.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.viewModel = viewModel
        binding.drawerLayout.apply {
            val toggle = ActionBarDrawerToggle(
                this@MainActivity, this, binding.appBar.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )

            addDrawerListener(toggle)
            toggle.syncState()
            toggle.isDrawerIndicatorEnabled = false
        }

        KeyboardVisibilityEvent.setEventListener(this) { isVisible ->
            binding.appBar.bottomNavigation.isVisible = !isVisible
        }

        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance())
                .commit()
        }

        viewModel.subscribe()
        viewModel.onActivityCreated()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount <= 0) {
                finish()
                return
            }

            super.onBackPressed()
        }
    }

    private fun MainViewModel.subscribe() {
        val activity = this@MainActivity

        replaceFragment.observe(activity, Observer {
            val newFragment = fragmentMap[it] ?: return@Observer

            supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.container, newFragment)
                .addToBackStack(null)
                .commit()
        })

        attachFragment.observe(activity, Observer {
//            title = getString(it.titleResId)
            binding.appBar.bottomNavigation.menu.findItem(it.menuItemId).isChecked = true
        })

        startPlanPostActivity.observe(activity, Observer {
            startActivity(PlanPostActivity.createIntent(activity))
        })

        startLoginActivity.observe(activity, Observer {
            val intent = LoginActivity.createIntent(activity).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            startActivity(intent)
            finish()
        })
    }
}
