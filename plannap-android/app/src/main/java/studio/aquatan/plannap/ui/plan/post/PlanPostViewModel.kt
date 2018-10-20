package studio.aquatan.plannap.ui.plan.post

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.ui.SingleLiveEvent


class PlanPostViewModel(
    private val planRepository: PlanRepository
) : ViewModel() {

    val postSpotList = MutableLiveData<List<PostSpot>>()

    val openFileChooser = SingleLiveEvent<Unit>()

    private var selectedId: Int? = null

    init {
        postSpotList.value = listOf(PostSpot(0))
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

    fun onImageSelected(bitmap: Bitmap) {
        val index = selectedId ?: return
        val list = postSpotList.value?.toMutableList() ?: return

        list[index] = list[index].copy(picture = bitmap)

        postSpotList.value = list
        selectedId = null
    }

    fun onPostClick() {

    }
}