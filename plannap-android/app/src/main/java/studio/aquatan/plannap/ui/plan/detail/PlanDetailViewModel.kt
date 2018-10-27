package studio.aquatan.plannap.ui.plan.detail

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import studio.aquatan.plannap.data.CommentRepository
import studio.aquatan.plannap.data.FavoriteRepository
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.model.Plan

class PlanDetailViewModel(
    private val planRepository: PlanRepository,
    private val favoriteRepository: FavoriteRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    private val planId = MutableLiveData<Long>()

    val plan: LiveData<Plan> = Transformations.switchMap(planId) { id ->
        planRepository.getPlanById(id)
    }

    val isSubmitting = ObservableBoolean()
    val isEnabledSubmit = ObservableBoolean()
    val comment = ObservableField<String>()
    val errorComment = ObservableField<String>()

    init {
        comment.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                isEnabledSubmit.set(!comment.get()?.trimEnd().isNullOrBlank())
            }
        })
    }

    fun onActivityCreated(id: Long) {
        planId.value = id
    }

    fun onFavoriteChanged(isFavorite: Boolean, count: Int) {
        val id = planId.value ?: return

        GlobalScope.launch {
            val isSuccess = if (isFavorite) {
                favoriteRepository.postFavorite(id)
            } else {
                favoriteRepository.deleteFavorite(id)
            }.await()

            if (!isSuccess) {
                planId.postValue(id)
            }
        }
    }

    fun onReportPostClick() {

    }

    fun onCommentSubmitClick() {
        val id = planId.value
        val text = comment.get()?.trimEnd() ?: ""

        if (id == null || text.isBlank()) {
            return
        }

        GlobalScope.launch {
            isSubmitting.set(true)
            errorComment.set(null)

            val (isSuccess, errorMessage) = commentRepository.postComment(id, text).await()
            if (isSuccess) {
                planId.postValue(id)
                comment.set("")
            } else {
                errorComment.set(errorMessage)
            }

            isSubmitting.set(false)
        }
    }
}