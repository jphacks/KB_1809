package studio.aquatan.plannap.ui.plan.searchresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.ui.SingleLiveEvent

class PlanSearchResultViewModel(
    private val planRepository: PlanRepository
) : ViewModel() {

    private val keyword = MutableLiveData<String>()

    val title = MutableLiveData<String>()

    val planList: LiveData<List<Plan>> = Transformations.switchMap(keyword) {
        title.value = it
        return@switchMap planRepository.getPlanListByKeyword(it)
    }
    val startPlanDetailActivity = SingleLiveEvent<Long>()

    fun onActivityCreated(areaName: String?) {
        keyword.value = areaName
    }

    fun onPlanClick(id: Long) {
        startPlanDetailActivity.value = id
    }
}