package studio.aquatan.plannap.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.ui.favorite.FavoriteFragment
import studio.aquatan.plannap.ui.favorite.FavoriteViewModel
import studio.aquatan.plannap.ui.home.HomeViewModel
import studio.aquatan.plannap.ui.main.MainViewModel
import studio.aquatan.plannap.ui.plan.post.PlanPostViewModel
import studio.aquatan.plannap.ui.plan.detail.PlanDetailViewModel
import studio.aquatan.plannap.ui.plan.list.PlanListViewModel
import studio.aquatan.plannap.ui.plan.search.PlanSearchViewModel
import studio.aquatan.plannap.ui.plan.searchresult.PlanSearchResultViewModel
import studio.aquatan.plannap.ui.profile.ProfileViewModel

class ViewModelFactory(
    private val context: Application,
    private val planRepository: PlanRepository
) : ViewModelProvider.AndroidViewModelFactory(context) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel()
                isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(planRepository)
                isAssignableFrom(FavoriteViewModel::class.java) -> FavoriteViewModel()
                isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel()
                isAssignableFrom(PlanListViewModel::class.java) -> PlanListViewModel(planRepository)
                isAssignableFrom(PlanDetailViewModel::class.java) -> PlanDetailViewModel(planRepository)
                isAssignableFrom(PlanSearchViewModel::class.java) -> PlanSearchViewModel()
                isAssignableFrom(PlanSearchResultViewModel::class.java) -> PlanSearchResultViewModel(planRepository)
                isAssignableFrom(PlanPostViewModel::class.java) -> PlanPostViewModel(planRepository)

                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}