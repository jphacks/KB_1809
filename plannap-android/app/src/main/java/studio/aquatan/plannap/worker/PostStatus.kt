package studio.aquatan.plannap.worker

import androidx.annotation.StringRes
import studio.aquatan.plannap.R

enum class PostStatus(
    @StringRes val resId: Int
) {

    LOADING(R.string.status_loading),
    POSTING(R.string.status_posting),
    SUCCESS(R.string.status_success),
    FAILED(R.string.status_failed)
}