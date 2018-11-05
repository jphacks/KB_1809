package studio.aquatan.plannap.ui.login

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import studio.aquatan.plannap.data.AuthRepository
import studio.aquatan.plannap.ui.SingleLiveEvent

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val username = ObservableField<String>()
    val password = ObservableField<String>()

    val isLoading = ObservableBoolean()
    val isVisibleError = ObservableBoolean()

    val isEnabledErrorUsername = SingleLiveEvent<Boolean>()
    val isEnabledErrorPassword = SingleLiveEvent<Boolean>()

    val validation = SingleLiveEvent<ValidationResult>()
    val hideSoftInput = SingleLiveEvent<Unit>()
    val startMainActivity = MutableLiveData<Unit>()

    init {
        username.setErrorCancelCallback(isEnabledErrorUsername)
        password.setErrorCancelCallback(isEnabledErrorPassword)
    }

    fun onLoginClick() {
        hideSoftInput.value = Unit

        val username = username.get() ?: ""
        val password = password.get() ?: ""

        var result = ValidationResult()
        if (username.isBlank()) {
            result = result.copy(isEmptyUsername = true)
        }
        if (password.isBlank()) {
            result = result.copy(isEmptyPassword = true)
        }

        if (result.isError) {
            validation.value = result
            return
        }

        GlobalScope.launch {
            isVisibleError.set(false)
            isLoading.set(true)

            val isSuccess = authRepository.login(username, password).await()

            if (isSuccess) {
                startMainActivity.postValue(Unit)
            } else {
                isVisibleError.set(true)
                isLoading.set(false)
            }
        }
    }

    private fun ObservableField<String>.setErrorCancelCallback(error: SingleLiveEvent<Boolean>) {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                error.value = false
            }
        })
    }
}