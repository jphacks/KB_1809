package studio.aquatan.plannap.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import studio.aquatan.plannap.data.Listing
import studio.aquatan.plannap.data.NetworkState
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.ui.SingleLiveEvent

class HomeViewModel(planRepository: PlanRepository) : ViewModel() {

    private val planListing: Listing<Plan> = planRepository.getPlanListing()

    val planList: LiveData<PagedList<Plan>> = planListing.pagedList
    val initialLoad: LiveData<NetworkState> = planListing.initialLoad
    val networkState: LiveData<NetworkState> = planListing.networkState

    val startPlanDetailActivity = SingleLiveEvent<Long>()

    fun onPlanClick(id: Long) {
        startPlanDetailActivity.value = id
    }

    fun onRefresh() {
        planListing.refresh.invoke()
    }

    fun onRetryClick() {
        planListing.retry.invoke()
    }
}
