package studio.aquatan.plannap.util

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.textfield.TextInputLayout
import studio.aquatan.plannap.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.LazyThreadSafetyMode.NONE

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

private val DATE_FORMAT by lazy(NONE) { SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.JAPAN) }

@BindingAdapter("date")
fun setDate(view: TextView, date: Date?) {
    view.text = if (date != null) {
        DATE_FORMAT.format(date)
    } else {
        null
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