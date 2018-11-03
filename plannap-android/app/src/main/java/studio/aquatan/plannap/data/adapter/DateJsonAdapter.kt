package studio.aquatan.plannap.data.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.Date

class DateJsonAdapter {
    @ToJson
    fun toJson(date: Date) = date.time.toString()

    @FromJson
    fun fromJson(unixTime: Long) = Date(unixTime * 1000L)
}