package studio.aquatan.plannap.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import studio.aquatan.plannap.data.api.SpotService
import studio.aquatan.plannap.data.model.Spot

class SpotRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://plannap.aquatan.studio")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = retrofit.create(SpotService::class.java)

    fun getSpotList(planId: Int): LiveData<List<Spot>> {
        val result = MutableLiveData<List<Spot>>()

        GlobalScope.launch {
            try {
                val response = service.getSpots(planId).execute()
                result.postValue(response.body() ?: emptyList())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch getSpots", e)
            }
        }

        return result
    }

    fun registerSpot(targetSpot: Spot) {
        GlobalScope.launch {
            try {
                service.postSpot(targetSpot).execute()
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch postSpot", e)
            }
        }
    }
}
