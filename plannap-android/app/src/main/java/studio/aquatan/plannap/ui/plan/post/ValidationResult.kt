package studio.aquatan.plannap.ui.plan.post

data class ValidationResult(
    val isEmptyName: Boolean = false,
    val isEmptyNote: Boolean = false,
    val isShortSpot: Boolean = false,
    val isInvalidSpot: Boolean = false
) {

    val isError = isEmptyName || isEmptyNote || isShortSpot || isInvalidSpot
}