package studio.aquatan.plannap.view.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton
import studio.aquatan.plannap.R

class FavoriteButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {

    private var onFavoriteChangedListener: ((Boolean, Int) -> Unit)? = null

    init {
        setOnClickListener {
            isFavorite = !isFavorite

            if (isFavorite) {
                count++
            } else {
                count--
            }

            onFavoriteChangedListener?.invoke(isFavorite, count)
        }
    }

    var isFavorite: Boolean = false
        set(value) {
            if (value) {
                setIconResource(R.drawable.ic_favorite_white_24dp)
            } else {
                setIconResource(R.drawable.ic_favorite_border_black_24dp)
            }

            field = value
        }

    var count: Int = 0
        set(value) {
            text = value.toString()
            field = value
        }

    fun setOnFavoriteChangedListener(listener: (Boolean, Int) -> Unit) {
        onFavoriteChangedListener = listener
    }
}