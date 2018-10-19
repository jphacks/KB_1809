package studio.aquatan.plannap.ui.plan.searchresult

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.aquatan.plannap.data.PlanRepository

class PlanSearchResultViewModel(
    private val planRepository: PlanRepository
) : ViewModel() {

    val title = MutableLiveData<String>()

    fun onActivityCreated(areaName: String?) {
        title.value = areaName
    }
}