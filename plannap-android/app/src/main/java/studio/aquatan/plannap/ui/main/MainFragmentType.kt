package studio.aquatan.plannap.ui.main

import androidx.annotation.StringRes
import studio.aquatan.plannap.R

enum class MainFragmentType(
    @StringRes val titleResId: Int,
    val menuItemId: Int
) {

    HOME(R.string.title_home, R.id.action_home),
    SEARCH(R.string.title_search, R.id.action_search),
    FAVORITE(R.string.title_favorite, R.id.action_favorite),
    PROFILE(R.string.title_profile, R.id.action_profile)
}