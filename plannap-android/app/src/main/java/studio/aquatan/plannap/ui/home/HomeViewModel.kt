package studio.aquatan.plannap.ui.home

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.ui.SingleLiveEvent

class HomeViewModel(
    private val planRepository: PlanRepository
) : ViewModel() {

    private val refreshRequest = SingleLiveEvent<Unit>()

    var planList = Transformations.switchMap(refreshRequest) {
        planRepository.getPlanList()
    }
    val startPlanDetailActivity = SingleLiveEvent<Long>()

    init {
        refreshRequest.value = Unit
    }

    fun onPlanClick(id: Long) {
        startPlanDetailActivity.value = id
    }

    fun onRefresh() {
        refreshRequest.value = Unit
    }
}
