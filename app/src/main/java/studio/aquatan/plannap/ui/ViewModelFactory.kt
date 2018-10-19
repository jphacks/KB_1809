package studio.aquatan.plannap.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.ui.plan.add.PlanAddViewModel
import studio.aquatan.plannap.ui.plan.detail.PlanDetailViewModel
import studio.aquatan.plannap.ui.plan.list.PlanListViewModel
import studio.aquatan.plannap.ui.plan.searchresult.PlanSearchResultViewModel

class ViewModelFactory(
    private val context: Application,
    private val planRepository: PlanRepository
) : ViewModelProvider.AndroidViewModelFactory(context) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(PlanListViewModel::class.java) ->
                    PlanListViewModel(planRepository)
                isAssignableFrom(PlanDetailViewModel::class.java) ->
                    PlanDetailViewModel(planRepository)
                isAssignableFrom(PlanSearchResultViewModel::class.java) ->
                    PlanSearchResultViewModel(planRepository)
                isAssignableFrom(PlanAddViewModel::class.java) ->
                    PlanAddViewModel(planRepository)

                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}