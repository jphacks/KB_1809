package studio.aquatan.plannap.ui.plan.searchresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import studio.aquatan.plannap.data.Listing
import studio.aquatan.plannap.data.NetworkState
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.ui.SingleLiveEvent

class PlanSearchResultViewModel(planRepository: PlanRepository) : ViewModel() {

    private val keyword = MutableLiveData<String>()

    val title = MutableLiveData<String>()

    private val planListing: LiveData<Listing<Plan>> = Transformations.map(keyword) {
        title.value = it
        return@map planRepository.searchPlanListing(it)
    }
    val planList: LiveData<PagedList<Plan>> = Transformations.switchMap(planListing) { it.pagedList }
    val initialLoad: LiveData<NetworkState> = Transformations.switchMap(planListing) { it.initialLoad }
    val networkState: LiveData<NetworkState> = Transformations.switchMap(planListing) { it.networkState }

    val startPlanDetailActivity = SingleLiveEvent<Long>()

    fun onActivityCreated(areaName: String?) {
        keyword.value = areaName
    }

    fun onPlanClick(id: Long) {
        startPlanDetailActivity.value = id
    }

    fun onRetryClick() {
        planListing.value?.retry?.invoke()
    }
}