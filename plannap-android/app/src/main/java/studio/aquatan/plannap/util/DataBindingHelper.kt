package studio.aquatan.plannap.util

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.textfield.TextInputLayout
import studio.aquatan.plannap.R

@BindingAdapter("isVisible")
fun setIsVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

@BindingAdapter("duration")
fun setDuration(view: TextView, duration: Int) {
    view.apply {
        text = context.getString(R.string.text_duration, duration)
    }
}

@BindingAdapter("cost")
fun setCost(view: TextView, cost: Int) {
    view.apply {
        text = context.getString(R.string.text_price, cost)
    }
}

@BindingAdapter("imageUri")
fun setImageUri(view: SimpleDraweeView, uri: String?) {
    view.setImageURI(uri)
}

@BindingAdapter("error")
fun setError(view: TextInputLayout, text: String?) {
    if (text.isNullOrBlank()) {
        view.isErrorEnabled = false
    } else {
        view.error = text
    }
}