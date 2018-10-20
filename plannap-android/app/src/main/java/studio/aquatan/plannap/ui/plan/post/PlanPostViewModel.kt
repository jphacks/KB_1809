package studio.aquatan.plannap.ui.plan.post

import android.graphics.Bitmap
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.ui.SingleLiveEvent


class PlanPostViewModel(
    private val planRepository: PlanRepository
) : ViewModel() {

    val name = ObservableField<String>()
    val note = ObservableField<String>()
    val duration = ObservableField<String>()
    val price = ObservableField<String>()

    val isEnabledErrorName = SingleLiveEvent<Boolean>()
    val isEnabledErrorNote = SingleLiveEvent<Boolean>()

    val postSpotList = MutableLiveData<List<PostSpot>>()

    val openFileChooser = SingleLiveEvent<Unit>()
    val validation = SingleLiveEvent<ValidationResult>()
    val errorSelectedImage = SingleLiveEvent<Unit>()

    private var selectedId: Int? = null

    init {
        postSpotList.value = listOf(PostSpot(0))

        name.setErrorCancelCallback(isEnabledErrorName)
        note.setErrorCancelCallback(isEnabledErrorNote)
    }

    fun onAddPostSpotClick() {
        val list = postSpotList.value?.toMutableList() ?: return

        list.add(PostSpot(list.size))

        postSpotList.value = list
    }

    fun onAddPictureClick(id: Int) {
        openFileChooser.value = Unit
        selectedId = id
    }

    fun onImageSelected(bitmap: Bitmap?, latLong: FloatArray?) {
        val index = selectedId ?: return
        selectedId = null

        if (bitmap == null || latLong == null || latLong.size < 2) {
            errorSelectedImage.value = Unit
            return
        }

        val list = postSpotList.value?.toMutableList() ?: return

        list[index] = list[index].copy(picture = bitmap, latLong = latLong)

        postSpotList.value = list
        selectedId = null
    }

    fun onPostClick() {
        val name = name.get()
        val note = note.get()
        val duration = duration.get()?.toIntOrNull()
        val price = price.get()?.toIntOrNull()
        val spotList = postSpotList.value ?: emptyList()

        var result = ValidationResult()

        if (name.isNullOrBlank()) {
            result = result.copy(isEmptyName = true)
        }
        if (note.isNullOrBlank()) {
            result = result.copy(isEmptyNote = true)
        }
        if (spotList.size < 2) {
            result = result.copy(isShortSpot = true)
        }
        if (spotList.any { it.name.isNullOrBlank() || it.note.isNullOrBlank() || it.picture == null || it.latLong == null }) {
            result = result.copy(isInvalidSpot = true)
        }

        if (result.isError) {
            validation.value = result
            return
        }

        // TODO post
    }

    private fun ObservableField<String>.setErrorCancelCallback(error: SingleLiveEvent<Boolean>) {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) { error.value = false }
        })
    }
}