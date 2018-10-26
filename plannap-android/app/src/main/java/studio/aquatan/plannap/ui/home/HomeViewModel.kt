package studio.aquatan.plannap.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import studio.aquatan.plannap.data.FavoriteRepository
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.ui.SingleLiveEvent

class HomeViewModel(
    private val planRepository: PlanRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val refreshRequest = SingleLiveEvent<Unit>()

    var planList: LiveData<List<Plan>> = Transformations.switchMap(refreshRequest) {
        planRepository.getPlanList()
    }
    val startPlanDetailActivity = SingleLiveEvent<Long>()

    init {
        refreshRequest.value = Unit
    }

    fun onPlanClick(id: Long) {
        startPlanDetailActivity.value = id
    }

    fun onFavoriteClick(id: Long, isFavorite: Boolean) {
        GlobalScope.launch {
            val isSuccess = if (isFavorite) {
                favoriteRepository.postFavorite(id)
            } else {
                favoriteRepository.deleteFavorite(id)
            }.await()

            if (!isSuccess) {
                refreshRequest.postValue(Unit)
            }
        }
    }

    fun onRefresh() {
        refreshRequest.value = Unit
    }
}
