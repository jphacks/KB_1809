package studio.aquatan.plannap.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.GlobalScope
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

    fun getFavoriteByPlan(planId: Long): LiveData<Favorite> {
        val result = MutableLiveData<Favorite>()

        GlobalScope.launch {
            try {
                val response = service.getFavoriteByPlanid(planId).execute()
                result.postValue(response.body())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch getFavoriteByPlanid", e)
            }
        }

        return result
    }

    fun getFavoriteByUser(userId: Long): LiveData<Favorite> {
        val result = MutableLiveData<Favorite>()

        GlobalScope.launch {
            try {
                val response = service.getFavoriteByPlanid(userId).execute()
                result.postValue(response.body())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch getFavoriteByUserid", e)
            }
        }

        return result
    }

    fun registerFavorite(targetFavorite: Favorite) {
        GlobalScope.launch {
            try {
                service.postFavorite(targetFavorite).execute()
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch postFavorite", e)
            }
        }
    }
}