package studio.aquatan.plannap.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
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

fun Bitmap.calcScaleWidthHeight(maxWidth: Double, maxHeight: Double): Pair<Int, Int> {
    var finalWidth = maxWidth.toInt()
    var finalHeight = maxHeight.toInt()

    val ratioBitmap = width.toDouble() / height.toDouble()
    val ratioMax = maxWidth / maxHeight

    if (ratioMax > ratioBitmap) {
        finalWidth = (maxHeight * ratioBitmap).toInt()
    } else {
        finalHeight = (maxWidth / ratioBitmap).toInt()
    }

    return finalWidth to finalHeight
}