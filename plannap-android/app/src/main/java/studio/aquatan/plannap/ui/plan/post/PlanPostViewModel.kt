package studio.aquatan.plannap.ui.plan.post

import android.graphics.Bitmap
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.model.EditableSpot
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

    val spotList = MutableLiveData<MutableList<EditableSpot>>()

    val openFileChooser = SingleLiveEvent<Unit>()
    val finishActivity = SingleLiveEvent<Unit>()
    val validation = SingleLiveEvent<ValidationResult>()
    val errorSelectedImage = SingleLiveEvent<Unit>()

    private var selectedSpotId: Int? = null

    init {
        spotList.value = mutableListOf(EditableSpot(0))

        name.setErrorCancelCallback(isEnabledErrorName)
        note.setErrorCancelCallback(isEnabledErrorNote)
    }

    fun onAddSpotClick() {
        val list = spotList.value ?: return
        list.add(EditableSpot(list.size))
        spotList.value = list
    }

    fun onAddPictureClick(id: Int) {
        openFileChooser.value = Unit
        selectedSpotId = id
    }

    fun onImageSelected(bitmap: Bitmap?, latLong: FloatArray?) {
        val index = selectedSpotId ?: return
        selectedSpotId = null

        if (bitmap == null || latLong == null || latLong.size < 2) {
            errorSelectedImage.value = Unit
            return
        }

        val list = spotList.value ?: return
        list[index] = list[index].copy(picture = bitmap, lat = latLong[0], lon = latLong[1])
        spotList.value = list
    }

    fun onPostClick() {
        val name = name.get() ?: ""
        val note = note.get() ?: ""
        val duration = duration.get()?.toIntOrNull()
        val price = price.get()?.toIntOrNull()
        val spotList = spotList.value ?: emptyList<EditableSpot>()

        var result = ValidationResult()

        if (name.isBlank()) {
            result = result.copy(isEmptyName = true)
        }
        if (note.isBlank()) {
            result = result.copy(isEmptyNote = true)
        }
        if (spotList.size < 2) {
            result = result.copy(isShortSpot = true)
        }
        if (spotList.any { it.name.isBlank() || it.note.isBlank() || it.picture == null || it.lat == null || it.lon == null }) {
            result = result.copy(isInvalidSpot = true)
        }

        if (result.isError) {
            validation.value = result
            return
        }

        GlobalScope.launch {
            val isSuccess = planRepository.registerPlan(name, note, duration, price, spotList).await()

            if (isSuccess) {
                finishActivity.postValue(Unit)
            }
        }
    }

    private fun ObservableField<String>.setErrorCancelCallback(error: SingleLiveEvent<Boolean>) {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) { error.value = false }
        })
    }
}