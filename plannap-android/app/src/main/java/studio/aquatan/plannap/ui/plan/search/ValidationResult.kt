package studio.aquatan.plannap.ui.plan.search

data class ValidationResult(
    val isAreaEmpty: Boolean = false
) {
    val isError = isAreaEmpty
}