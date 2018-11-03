package studio.aquatan.plannap.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import studio.aquatan.plannap.data.Listing
import studio.aquatan.plannap.data.NetworkState
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.ui.SingleLiveEvent

class FavoriteViewModel(planRepository: PlanRepository) : ViewModel() {

    private var favoritePlanListing: Listing<Plan> = planRepository.getFavoritePlanListing()

    val favoritePlanList: LiveData<PagedList<Plan>> = favoritePlanListing.pagedList
    val initialLoad: LiveData<NetworkState> = favoritePlanListing.initialLoad
    val networkState: LiveData<NetworkState> = favoritePlanListing.networkState

    val startPlanDetailActivity = SingleLiveEvent<Long>()

    fun onPlanClick(id: Long) {
        startPlanDetailActivity.value = id
    }

    fun onRefresh() {
        favoritePlanListing.refresh.invoke()
    }

    fun onRetryClick() {
        favoritePlanListing.retry.invoke()
    }
}
