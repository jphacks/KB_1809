package studio.aquatan.plannap.ui.plan.list

import android.util.Log
import androidx.lifecycle.ViewModel
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.ui.SingleLiveEvent

class PlanListViewModel(
    private val planRepository: PlanRepository
) : ViewModel() {

    val planList = planRepository.getPlanList()
    val startPlanDetailActivity = SingleLiveEvent<Long>()

    fun onActivityCreated() {
    }

    fun onPlanClick(id: Long) {
        Log.d(javaClass.simpleName, "onPlanClick: $id")
        startPlanDetailActivity.value = id
    }
}
