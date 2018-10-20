package studio.aquatan.plannap.ui.plan.searchresult

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import studio.aquatan.plannap.data.PlanRepository

class PlanSearchResultViewModel(
    private val planRepository: PlanRepository
) : ViewModel() {

    private val keyword = MutableLiveData<String>()

    val title = MutableLiveData<String>()

    val planList = Transformations.switchMap(keyword) {
        title.value = it
        return@switchMap planRepository.getPlanByKeyword(it)
    }

    fun onActivityCreated(areaName: String?) {
        keyword.value = areaName
    }

    fun onPlanClick(id: Long) {

    }
}