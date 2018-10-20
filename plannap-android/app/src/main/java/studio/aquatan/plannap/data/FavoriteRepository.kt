package studio.aquatan.Favoritenap.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import studio.aquatan.plannap.data.api.FavoriteService
import studio.aquatan.plannap.data.model.Favorite

class FavoriteRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://plannap.aquatan.studio")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = retrofit.create(FavoriteService::class.java)

    fun getFavoriteList(): LiveData<List<Favorite>> {
        val result = MutableLiveData<List<Favorite>>()

        GlobalScope.launch {
            // TODO fetch Favorite list via API
            delay(2000)
//            result.postValue(DUMMY_LIST)

//            try {
//                val response = service.plans().execute()
//                result.postValue(response.body())
//            } catch (e: Exception) {
//                Log.e(javaClass.simpleName, "Failed to fetch plans", e)
//            }
        }

        return result
    }

    fun getFavoriteById(id: Long): LiveData<Favorite> {
        val result = MutableLiveData<Favorite>()

        GlobalScope.launch {
            // TODO fetch Favorite via API
            delay(1000)
//            result.postValue(DUMMY_LIST.find { it.id == id })
        }

        return result
    }
}