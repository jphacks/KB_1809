package studio.aquatan.plannap.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import studio.aquatan.plannap.Session
import studio.aquatan.plannap.data.api.FavoriteService
import studio.aquatan.plannap.data.model.Favorite
import studio.aquatan.plannap.data.model.PostFavorite

class FavoriteRepository(session: Session) : BaseRepository(session) {

    private val service = buildRetrofit().create(FavoriteService::class.java)

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

    fun postFavorite(planId: Long) =
        GlobalScope.async {
            try {
                val response = service.postFavorite(PostFavorite(planId)).execute()
                return@async response.code() == 200
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to post Favorite", e)
            }

            return@async false
        }

    fun deleteFavorite(planId: Long) =
        GlobalScope.async {
            try {
                val response = service.deleteFavorite(PostFavorite(planId)).execute()
                return@async response.code() == 204
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to delete Favorite", e)
            }

            return@async false
        }
}