package studio.aquatan.plannap.ui.plan.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.model.Plan

class PlanDetailViewModel(
    private val planRepository: PlanRepository
) : ViewModel() {

    private val planId = MutableLiveData<Long>()

    val plan: LiveData<Plan> = Transformations.switchMap(planId) { id ->
        planRepository.getPlanById(id)
    }

    fun onActivityCreated(id: Long) {
        planId.value = id
    }
}