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
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.model.Plan

class PlanDetailViewModel(
    private val planRepository: PlanRepository,
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
                isEnabledSubmit.set(!comment.get().isNullOrBlank())
            }
        })
    }

    fun onActivityCreated(id: Long) {
        planId.value = id
    }

    fun onReportPostClick() {

    }

    fun onCommentSubmitClick() {
        val planId = planId.value
        val text = comment.get() ?: ""

        if (planId == null || text.isBlank()) {
            return
        }

        GlobalScope.launch {
            isSubmitting.set(true)
            errorComment.set(null)

            val isSuccess = commentRepository.postComment(planId, text).await()
            if (isSuccess) {
                comment.set("")
            } else {
                errorComment.set("TODO")
            }

            isSubmitting.set(false)
        }
    }
}