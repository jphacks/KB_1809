package studio.aquatan.plannap.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import studio.aquatan.plannap.GlideApp
import studio.aquatan.plannap.R

@BindingAdapter("isVisible")
fun setIsVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

@BindingAdapter("price")
fun setMoney(view: TextView, price: Int) {
    view.apply {
        text = context.getString(R.string.text_price, price)
    }
}

@BindingAdapter("duration")
fun setDuration(view: TextView, duration: Int) {
    view.apply {
        text = context.getString(R.string.text_duration, duration)
    }
}

@BindingAdapter("srcUrl")
fun setSrcUrl(view: ImageView, url: String?) {
    url ?: return

    GlideApp.with(view.context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

@BindingAdapter("iconUrl")
fun setIconUrl(view: ImageView, url: String?) {
    url ?: return

    GlideApp.with(view.context)
        .load(url)
        .circleCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}