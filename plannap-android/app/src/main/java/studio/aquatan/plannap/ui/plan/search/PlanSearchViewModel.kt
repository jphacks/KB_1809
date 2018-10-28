package studio.aquatan.plannap.ui.plan.search

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import studio.aquatan.plannap.ui.SingleLiveEvent

class PlanSearchViewModel : ViewModel() {

    val area = ObservableField<String>()
    val isEnabledErrorArea = SingleLiveEvent<Boolean>()

    val validation = SingleLiveEvent<ValidationResult>()
    val startSearchResultActivity = SingleLiveEvent<String>()

    init {
        area.setErrorCancelCallback(isEnabledErrorArea)
    }

    fun onSearchClick() {
        val areaName = area.get()

        if (areaName.isNullOrBlank()) {
            validation.value = ValidationResult(isAreaEmpty = true)
            return
        }

        startSearchResultActivity.value = areaName
    }

    private fun ObservableField<String>.setErrorCancelCallback(error: SingleLiveEvent<Boolean>) {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                error.value = false
            }
        })
    }
}