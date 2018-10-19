package studio.aquatan.plannap.ui.plan.search

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import studio.aquatan.plannap.ui.SingleLiveEvent

class PlanSearchViewModel : ViewModel() {

    val area = ObservableField<String>()

    val startSearchResultActivity = SingleLiveEvent<String>()

    fun onSearchClick() {
        startSearchResultActivity.value = area.get() ?: "TODO: validation"
    }
}