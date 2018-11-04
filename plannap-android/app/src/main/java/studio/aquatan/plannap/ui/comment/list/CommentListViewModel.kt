package studio.aquatan.plannap.ui.comment.list

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import studio.aquatan.plannap.data.CommentRepository
import studio.aquatan.plannap.data.Listing
import studio.aquatan.plannap.data.NetworkState
import studio.aquatan.plannap.data.model.Comment

class CommentListViewModel(
    private val commentRepository: CommentRepository
) : ViewModel() {

    private val planId = MutableLiveData<Long>()

    private val commentListing: LiveData<Listing<Comment>> = Transformations.map(planId) {
        commentRepository.getCommentListing(it)
    }
    val commentList: LiveData<PagedList<Comment>> = Transformations.switchMap(commentListing) { it.pagedList }
    val initialLoad: LiveData<NetworkState> = Transformations.switchMap(commentListing) { it.initialLoad }
    val networkState: LiveData<NetworkState> = Transformations.switchMap(commentListing) { it.networkState }

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

    fun onSubmitClick() {
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

    fun onRefresh() {
        commentListing.value?.refresh?.invoke()
    }

    fun onRetryClick() {
        commentListing.value?.retry?.invoke()
    }
}