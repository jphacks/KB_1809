package studio.aquatan.plannap.ui.plan.post

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.model.EditableSpot
import studio.aquatan.plannap.ui.SingleLiveEvent
import studio.aquatan.plannap.worker.PostPlanWorker
import java.io.IOException


class PlanPostViewModel(
    private val context: Application,
    private val planRepository: PlanRepository
) : AndroidViewModel(context) {

    companion object {
        private const val MAX_IMAGE_WIDTH = 1920.0
        private const val MAX_IMAGE_HEIGHT = 1920.0
    }

    val name = ObservableField<String>()
    val note = ObservableField<String>()
    val duration = ObservableField<String>()
    val price = ObservableField<String>()

    val isLoading = ObservableBoolean()

    val isEnabledErrorName = SingleLiveEvent<Boolean>()
    val isEnabledErrorNote = SingleLiveEvent<Boolean>()

    private val _spotList = mutableListOf(EditableSpot(0), EditableSpot(1))
    val spotList = MutableLiveData<List<EditableSpot>>()

    val openFileChooser = SingleLiveEvent<Unit>()
    val finishActivity = SingleLiveEvent<Unit>()
    val validation = SingleLiveEvent<ValidationResult>()
    val errorSelectedImage = SingleLiveEvent<Unit>()

    private var selectedSpotId: Int? = null

    init {
        spotList.value = _spotList

        name.setErrorCancelCallback(isEnabledErrorName)
        note.setErrorCancelCallback(isEnabledErrorNote)
    }

    fun onAddSpotClick() {
        val nextId = _spotList.size
        _spotList.add(EditableSpot(nextId))
        spotList.value = _spotList
    }

    fun onAddPictureClick(id: Int) {
        openFileChooser.value = Unit
        selectedSpotId = id
    }

    fun onImageSelected(uri: Uri) {
        val index = selectedSpotId ?: return
        selectedSpotId = null

//        val bitmap = uri.toBitmap()
        val latLong = uri.toLatLong()

        if (latLong == null || latLong.size < 2) {
            errorSelectedImage.value = Unit
            return
        }

        _spotList[index] = _spotList[index].copy(imageUri = uri, lat = latLong[0], long = latLong[1])
        spotList.value = _spotList
    }

    fun onPostClick() {
        if (isLoading.get()) {
            return
        }

        val name = name.get() ?: ""
        val note = note.get() ?: ""
        val duration = duration.get()?.toIntOrNull() ?: 0
        val price = price.get()?.toIntOrNull() ?: 0
        val spotList = _spotList

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
        if (spotList.any { it.name.isBlank() || it.note.isBlank() || it.imageUri == null }) {
            result = result.copy(isInvalidSpot = true)
        }

        if (result.isError) {
            validation.value = result
            return
        }

        GlobalScope.launch {
            isLoading.set(true)

            val uuid = planRepository.savePlanToFile(name, note, duration, price, spotList).await()

            val data = Data.Builder().apply {
                putString(PostPlanWorker.KEY_TITLE, name)
                putString(PostPlanWorker.KEY_OUTPUT_UUID, uuid.toString())
            }.build()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val postRequest = OneTimeWorkRequest.Builder(PostPlanWorker::class.java)
                .setInputData(data)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance().enqueue(postRequest)

            finishActivity.postValue(Unit)
        }
    }

    private fun Uri.toLatLong(): DoubleArray? {
        val stream = context.contentResolver.openInputStream(this) ?: return null
        try {
            val exifInterface = ExifInterface(stream)
            return exifInterface.latLong
        } catch (e: IOException) {
            Log.e(javaClass.simpleName, "Failed to get LatLong", e)
        } finally {
            stream.close()
        }

        return null
    }

    private fun ObservableField<String>.setErrorCancelCallback(error: SingleLiveEvent<Boolean>) {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) { error.value = false }
        })
    }
}