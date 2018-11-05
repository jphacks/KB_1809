package studio.aquatan.plannap.ui.plan.post

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.model.EditableSpot
import studio.aquatan.plannap.ui.SingleLiveEvent
import studio.aquatan.plannap.worker.PostPlanWorker
import java.io.InputStream

class PlanPostViewModel(
    private val context: Application,
    private val planRepository: PlanRepository
) : AndroidViewModel(context) {

    val name = ObservableField<String>()
    val note = ObservableField<String>()
    val duration = ObservableField<String>()
    val cost = ObservableField<String>()

    val isLoading = ObservableBoolean()

    val isEnabledErrorName = SingleLiveEvent<Boolean>()
    val isEnabledErrorNote = SingleLiveEvent<Boolean>()
    val isEnabledErrorDuration = SingleLiveEvent<Boolean>()
    val isEnabledErrorCost = SingleLiveEvent<Boolean>()

    val editableSpotList = MutableLiveData<List<EditableSpot>>()

    val openFileChooser = SingleLiveEvent<Intent>()
    val finishActivity = SingleLiveEvent<Unit>()
    val validation = SingleLiveEvent<ValidationResult>()
    val errorSelectedImage = SingleLiveEvent<Unit>()

    private var selectedSpotId: Int? = null

    init {
        editableSpotList.value = mutableListOf(EditableSpot(0), EditableSpot(1))

        name.setErrorCancelCallback(isEnabledErrorName)
        note.setErrorCancelCallback(isEnabledErrorNote)
        duration.setErrorCancelCallback(isEnabledErrorDuration)
        cost.setErrorCancelCallback(isEnabledErrorCost)
    }

    fun onAddPictureClick(id: Int) {
        openFileChooser.value = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }

        selectedSpotId = id
    }

    fun onAddSpotClick() {
        val list = editableSpotList.value?.toMutableList() ?: return

        val nextId = list.size
        list.add(EditableSpot(nextId))
        editableSpotList.value = list
    }

    fun onImageSelected(uri: Uri) {
        val index = selectedSpotId ?: return
        selectedSpotId = null

        val latLong = uri.toLatLong()

        if (latLong == null || latLong.size < 2) {
            errorSelectedImage.value = Unit
            return
        }

        val list = editableSpotList.value?.toMutableList() ?: return

        list[index] = list[index].copy(imageUri = uri, lat = latLong[0], long = latLong[1])

        editableSpotList.value = list
    }

    fun onPostClick() {
        if (isLoading.get()) {
            return
        }

        val name = name.get() ?: ""
        val note = note.get() ?: ""
        val duration = duration.get()?.toIntOrNull() ?: -1
        val cost = cost.get()?.toIntOrNull() ?: -1
        val spotList = editableSpotList.value ?: emptyList()

        if (!validate(name, note, duration, cost, spotList)) {
            return
        }

        GlobalScope.launch {
            isLoading.set(true)

            val uuid = planRepository.savePlanToFile(name, note, duration, cost, spotList).await()

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

    private fun validate(
        name: String, note: String, duration: Int, cost: Int,
        spotList: List<EditableSpot>
    ): Boolean {
        var result = ValidationResult(
            isEmptyName = name.isBlank(),
            isEmptyNote = note.isBlank(),
            isShortDuration = duration <= 0,
            isShortCost = cost < 0,
            isShortSpot = spotList.size < 2
        )

        val list = spotList.map {
            return@map it.copy(isError = it.imageUri == null || it.name.isBlank() || it.note.isBlank())
        }

        if (list.any { it.isError }) {
            editableSpotList.value = list
            result = result.copy(isInvalidSpot = true)
        }

        if (result.isError) {
            validation.value = result
        }

        return !result.isError
    }

    private fun Uri.toLatLong(): DoubleArray? {
        var stream: InputStream? = null
        try {
            stream = context.contentResolver.openInputStream(this)

            val exifInterface = ExifInterface(stream)
            return exifInterface.latLong
        } catch (e: Exception) {
            Log.e("PlanPostViewModel", "Failed to get LatLong", e)
        } finally {
            stream?.close()
        }

        return null
    }

    private fun ObservableField<String>.setErrorCancelCallback(error: SingleLiveEvent<Boolean>) {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                error.value = false
            }
        })
    }
}