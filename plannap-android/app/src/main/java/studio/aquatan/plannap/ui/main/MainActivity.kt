package studio.aquatan.plannap.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_main.*
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ActivityMainBinding
import studio.aquatan.plannap.ui.plan.add.PlanAddActivity
import studio.aquatan.plannap.ui.plan.list.PlanListFragment
import studio.aquatan.plannap.ui.plan.search.PlanSearchActivity

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.setLifecycleOwner(this)
        setSupportActionBar(binding.appBar.toolbar)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

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
                .replace(R.id.container, PlanListFragment.newInstance())
                .commit()
        }


        viewModel.subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> viewModel.onSearchClick()
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_plans -> {

            }
            R.id.nav_settings -> {

            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun MainViewModel.subscribe() {
        val activity = this@MainActivity

        startSearchActivity.observe(activity, Observer {
            startActivity(PlanSearchActivity.createIntent(activity))
        })
        startPlanAddActivity.observe(activity, Observer {
            startActivity(PlanAddActivity.createIntent(activity))
        })
    }
}
