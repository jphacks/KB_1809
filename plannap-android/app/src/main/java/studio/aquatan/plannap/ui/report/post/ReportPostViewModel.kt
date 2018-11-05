package studio.aquatan.plannap.ui.report.post

import android.content.Intent
import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import studio.aquatan.plannap.data.ReportRepository
import studio.aquatan.plannap.ui.SingleLiveEvent
import studio.aquatan.plannap.ui.plan.post.ValidationResult

class ReportPostViewModel(
    private val reportRepository: ReportRepository
) : ViewModel() {

    private val planId = MutableLiveData<Long>()
    private val imageUri = MutableLiveData<Uri>()

    val isSubmitting = ObservableBoolean()
    val isEnabledSubmit = ObservableBoolean()
    val note = ObservableField<String>()
    val errorComment = ObservableField<String>()

    val openFileChooser = SingleLiveEvent<Intent>()
    val finishActivity = SingleLiveEvent<Unit>()
    val validation = SingleLiveEvent<ValidationResult>()

    fun onActivityCreated(id: Long) {
        planId.value = id
    }

    fun onAddPictureClick() {
        openFileChooser.value = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
    }

    fun onImageSelected(uri: Uri) {
        imageUri.value = uri
    }

    fun onSubmitClick() {
        val id = planId.value ?: 0
        val text = note.get() ?: ""

        if (text.isBlank()) {
            return
        }

        GlobalScope.launch {
            isSubmitting.set(true)
            errorComment.set(null)

            val (isSuccess, errorMessage) = reportRepository.postReport(id, text, imageUri.value!!).await()
            if (isSuccess) {
                planId.postValue(id)
                note.set("")
            } else {
                errorComment.set(errorMessage)
            }

            isSubmitting.set(false)
        }
    }
}