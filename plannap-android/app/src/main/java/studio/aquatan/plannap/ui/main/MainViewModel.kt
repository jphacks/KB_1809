package studio.aquatan.plannap.ui.main

import androidx.lifecycle.ViewModel
import studio.aquatan.plannap.ui.SingleLiveEvent

class MainViewModel : ViewModel() {

    val startSearchActivity = SingleLiveEvent<Unit>()
    val startPlanPostActivity = SingleLiveEvent<Unit>()

    fun onSearchClick() {
        startSearchActivity.value = Unit
    }

    fun onPlanPostClick() {
        startPlanPostActivity.value = Unit
    }
}