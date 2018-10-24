package studio.aquatan.plannap.ui.login

data class ValidationResult(
    val isEmptyUsername: Boolean = false,
    val isEmptyPassword: Boolean = false
) {
    val isError = isEmptyUsername || isEmptyPassword
}