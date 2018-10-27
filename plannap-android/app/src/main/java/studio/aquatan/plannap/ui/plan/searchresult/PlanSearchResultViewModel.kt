package studio.aquatan.plannap.ui.plan.searchresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import studio.aquatan.plannap.data.FavoriteRepository
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.ui.SingleLiveEvent

class PlanSearchResultViewModel(
    private val planRepository: PlanRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val keyword = MutableLiveData<String>()

    val title = MutableLiveData<String>()

    val planList: LiveData<List<Plan>> = Transformations.switchMap(keyword) {
        title.value = it
        return@switchMap planRepository.getPlanListByKeyword(it)
    }
    val startPlanDetailActivity = SingleLiveEvent<Long>()
    val startCommentListActivity = SingleLiveEvent<Pair<Long, String>>()

    fun onActivityCreated(areaName: String?) {
        keyword.value = areaName
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
                keyword.postValue(keyword.value)
            }
        }
    }

    fun onCommentClick(id: Long, name: String) {
        startCommentListActivity.value = id to name
    }
}