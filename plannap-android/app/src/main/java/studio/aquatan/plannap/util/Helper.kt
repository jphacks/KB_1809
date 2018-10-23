package studio.aquatan.plannap.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Activity.hideSoftInput() {
    try {
        val view = this.currentFocus ?: return

        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!inputMethodManager.isActive) {
            return
        }
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}