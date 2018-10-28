package studio.aquatan.plannap.data.adapter

import android.net.Uri
import androidx.core.net.toUri
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson


class UriJsonAdapter {
    @ToJson
    fun toJson(uri: Uri) = uri.toString()

    @FromJson
    fun fromJson(uri: String) = uri.toUri()
}