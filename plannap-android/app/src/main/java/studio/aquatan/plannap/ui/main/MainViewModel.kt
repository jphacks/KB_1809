package studio.aquatan.plannap.ui.main

import android.view.MenuItem
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import studio.aquatan.plannap.R
import studio.aquatan.plannap.ui.SingleLiveEvent

class MainViewModel : ViewModel(), BottomNavigationView.OnNavigationItemSelectedListener {

    val attachFragment = SingleLiveEvent<MainFragmentType>()
    val replaceFragment = SingleLiveEvent<MainFragmentType>()

    val startPlanPostActivity = SingleLiveEvent<Unit>()

    private var currentFragment = MainFragmentType.HOME

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> submitFragmentIfNeeded(MainFragmentType.HOME)
            R.id.action_search -> submitFragmentIfNeeded(MainFragmentType.SEARCH)
            R.id.action_post -> {
                item.isCheckable = false
                startPlanPostActivity.value = Unit
            }
            R.id.action_favorite -> submitFragmentIfNeeded(MainFragmentType.FAVORITE)
            R.id.action_profile -> submitFragmentIfNeeded(MainFragmentType.PROFILE)
        }

        return true
    }

    fun onAttachFragment(type: MainFragmentType) {
        currentFragment = type
        attachFragment.value = type
    }

    private fun submitFragmentIfNeeded(type: MainFragmentType) {
        if (currentFragment != type) {
            currentFragment = type

            replaceFragment.value = type
            attachFragment.value = type
        }
    }
}