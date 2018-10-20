package studio.aquatan.plannap.ui.main

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ActivityMainBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import studio.aquatan.plannap.ui.home.HomeFragment
import studio.aquatan.plannap.ui.plan.list.PlanListFragment
import studio.aquatan.plannap.ui.plan.post.PlanPostActivity
import studio.aquatan.plannap.ui.plan.search.PlanSearchActivity
import studio.aquatan.plannap.ui.plan.search.PlanSearchFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private val fragmentMap by lazy(LazyThreadSafetyMode.NONE) {
        mapOf(
            MainFragmentType.HOME to HomeFragment.newInstance(),
            MainFragmentType.SEARCH to PlanSearchFragment.newInstance(),
            MainFragmentType.FAVORITE to PlanListFragment.newInstance(),
            MainFragmentType.PROFILE to PlanListFragment.newInstance()
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

        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance())
                .commit()
        }

        viewModel.subscribe()
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

        startSearchActivity.observe(activity, Observer {
            startActivity(PlanSearchActivity.createIntent(activity))
        })
        startPlanPostActivity.observe(activity, Observer {
            startActivity(PlanPostActivity.createIntent(activity))
        })
    }
}
