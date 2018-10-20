package studio.aquatan.plannap.ui.home

import androidx.lifecycle.ViewModel
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.ui.SingleLiveEvent

class HomeViewModel(
    private val planRepository: PlanRepository
) : ViewModel() {

    val planList = planRepository.getPlanList()
    val startPlanDetailActivity = SingleLiveEvent<Long>()

    fun onPlanClick(id: Long) {
        startPlanDetailActivity.value = id
    }
}
