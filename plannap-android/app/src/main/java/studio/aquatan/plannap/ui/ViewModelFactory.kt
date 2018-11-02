package studio.aquatan.plannap.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import studio.aquatan.plannap.data.AuthRepository
import studio.aquatan.plannap.data.CommentRepository
import studio.aquatan.plannap.data.FavoriteRepository
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.UserRepository
import studio.aquatan.plannap.ui.comment.list.CommentListViewModel
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
    private val planRepo: PlanRepository,
    private val favoriteRepo: FavoriteRepository,
    private val commentRepo: CommentRepository,
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository
) : ViewModelProvider.AndroidViewModelFactory(context) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel(authRepo)
                isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(planRepo)
                isAssignableFrom(FavoriteViewModel::class.java) -> FavoriteViewModel()
                isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(planRepo, favoriteRepo)
                isAssignableFrom(PlanDetailViewModel::class.java) -> PlanDetailViewModel(
                    planRepo, favoriteRepo,
                    commentRepo
                )
                isAssignableFrom(PlanSearchViewModel::class.java) -> PlanSearchViewModel()
                isAssignableFrom(PlanSearchResultViewModel::class.java) -> PlanSearchResultViewModel(
                    planRepo
                )
                isAssignableFrom(PlanPostViewModel::class.java) -> PlanPostViewModel(context, planRepo)
                isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(authRepo)
                isAssignableFrom(CommentListViewModel::class.java) -> CommentListViewModel(commentRepo)

                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}