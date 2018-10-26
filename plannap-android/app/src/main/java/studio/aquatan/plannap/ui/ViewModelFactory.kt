package studio.aquatan.plannap.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import studio.aquatan.plannap.data.AuthRepository
import studio.aquatan.plannap.data.CommentRepository
import studio.aquatan.plannap.data.FavoriteRepository
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.ui.favorite.FavoriteViewModel
import studio.aquatan.plannap.ui.home.HomeViewModel
import studio.aquatan.plannap.ui.login.LoginViewModel
import studio.aquatan.plannap.ui.main.MainViewModel
import studio.aquatan.plannap.ui.plan.detail.PlanDetailViewModel
import studio.aquatan.plannap.ui.plan.post.PlanPostViewModel
import studio.aquatan.plannap.ui.plan.search.PlanSearchViewModel
import studio.aquatan.plannap.ui.plan.searchresult.PlanSearchResultViewModel
import studio.aquatan.plannap.ui.profile.ProfileViewModel

class ViewModelFactory(
    private val context: Application,
    private val planRepository: PlanRepository,
    private val favoriteRepository: FavoriteRepository,
    private val commentRepository: CommentRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.AndroidViewModelFactory(context) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel(authRepository)
                isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(planRepository, favoriteRepository)
                isAssignableFrom(FavoriteViewModel::class.java) -> FavoriteViewModel()
                isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel()
                isAssignableFrom(PlanDetailViewModel::class.java) -> PlanDetailViewModel(planRepository, commentRepository)
                isAssignableFrom(PlanSearchViewModel::class.java) -> PlanSearchViewModel()
                isAssignableFrom(PlanSearchResultViewModel::class.java) -> PlanSearchResultViewModel(planRepository, favoriteRepository)
                isAssignableFrom(PlanPostViewModel::class.java) -> PlanPostViewModel(context, planRepository)
                isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(authRepository)

                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}