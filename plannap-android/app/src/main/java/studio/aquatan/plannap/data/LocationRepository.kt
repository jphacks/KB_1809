package studio.aquatan.plannap.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import studio.aquatan.plannap.data.api.LocationService
import studio.aquatan.plannap.data.model.Location

class LocationRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://plannap.aquatan.studio")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = retrofit.create(LocationService::class.java)

    fun getLocationList(locationId: Long): LiveData<Location> {
        val result = MutableLiveData<Location>()

        GlobalScope.launch {
            try {
                val response = service.getLocation(locationId).execute()
                result.postValue(response.body())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch getLocation", e)
            }
        }

        return result
    }
}