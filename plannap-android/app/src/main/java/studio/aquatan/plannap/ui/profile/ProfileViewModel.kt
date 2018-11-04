package studio.aquatan.plannap.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import studio.aquatan.plannap.data.Listing
import studio.aquatan.plannap.data.NetworkState
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.UserRepository
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.data.model.User
import studio.aquatan.plannap.ui.SingleLiveEvent

class ProfileViewModel(planRepository: PlanRepository, userRepository: UserRepository) : ViewModel() {

    private val planListing: Listing<Plan> = planRepository.getMyPlanListing()
    private val refresh = MutableLiveData<Unit>()

    val planList: LiveData<PagedList<Plan>> = planListing.pagedList
    val initialLoad: LiveData<NetworkState> = planListing.initialLoad
    val networkState: LiveData<NetworkState> = planListing.networkState
    val user: LiveData<User> = Transformations.switchMap(refresh) { userRepository.getUser() }

    val startPlanDetailActivity = SingleLiveEvent<Long>()

    init {
        refresh.value = Unit
    }

    fun onPlanClick(id: Long) {
        startPlanDetailActivity.value = id
    }

    fun onRefresh() {
        refresh.value = Unit
        planListing.refresh.invoke()
    }

    fun onRetryClick() {
        planListing.retry.invoke()
    }
}
