package studio.aquatan.plannap.data

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import studio.aquatan.plannap.Session
import studio.aquatan.plannap.data.api.PlanService
import studio.aquatan.plannap.data.model.EditableSpot
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.data.model.PostPlan
import studio.aquatan.plannap.data.model.PostSpot
import java.io.ByteArrayOutputStream

class PlanRepository(session: Session): BaseRepository(session) {

    private val service = buildRetrofit().create(PlanService::class.java)

    fun getPlanList(): LiveData<List<Plan>> {
        val result = MutableLiveData<List<Plan>>()

        GlobalScope.launch {
            try {
                val response = service.getPlans().execute()
                result.postValue(response.body() ?: emptyList())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch plans", e)
            }
        }

        return result
    }

    fun getPlanById(id: Long): LiveData<Plan> {
        val result = MutableLiveData<Plan>()

        GlobalScope.launch {
            try {
                val response = service.getPlan(id).execute()
                result.postValue(response.body())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch getPlan", e)
            }
        }

        return result
    }

    fun getPlanListByKeyword(keyword: String): LiveData<List<Plan>> {
        val result = MutableLiveData<List<Plan>>()

        GlobalScope.launch {
            try {
                val response = service.getPlan(keyword).execute()
                result.postValue(response.body() ?: emptyList())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch getPlan", e)
            }
        }

        return result
    }

    fun postPlan(name: String, note: String, duration: Int, price: Int, editableSpotList: List<EditableSpot>) =
        GlobalScope.async {
            val spotList = editableSpotList.map { spot ->
                val image = spot.image ?: return@async false

                val stream = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.PNG, 85, stream)
                val bytes = stream.toByteArray()

                return@map PostSpot(
                    spot.name,
                    spot.note,
                    Base64.encodeToString(bytes, Base64.DEFAULT),
                    spot.lat,
                    spot.long)
            }

            try {
                val postPlan = PostPlan(name, price, duration, note, spotList)

                val response = service.postPlan(postPlan).execute()
                Log.d(javaClass.simpleName, "response code: ${response.code()}")

                return@async response.code() == 201
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to post a Plan", e)
            }

            return@async false
        }
}