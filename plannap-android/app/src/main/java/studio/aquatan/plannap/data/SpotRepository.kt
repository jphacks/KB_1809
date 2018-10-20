package studio.aquatan.plannap.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
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

    fun getSpotList(): LiveData<List<Spot>> {
        val result = MutableLiveData<List<Spot>>()

        GlobalScope.launch {
            // TODO fetch Spot list via API
            delay(2000)
            result.postValue(DUMMY_LIST)

//            try {
//                val response = service.plans().execute()
//                result.postValue(response.body())
//            } catch (e: Exception) {
//                Log.e(javaClass.simpleName, "Failed to fetch plans", e)
//            }
        }

        return result
    }

    fun getSpotById(id: Long): LiveData<Spot> {
        val result = MutableLiveData<Spot>()

        GlobalScope.launch {
            // TODO fetch Spot via API
            delay(1000)
            result.postValue(DUMMY_LIST.find { it.id == id })
        }

        return result
    }
}
}