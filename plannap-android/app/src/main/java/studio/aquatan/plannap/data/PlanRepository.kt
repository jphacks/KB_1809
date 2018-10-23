package studio.aquatan.plannap.data

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import studio.aquatan.plannap.data.api.PlanService
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.data.model.EditableSpot
import studio.aquatan.plannap.data.model.PostPlan
import studio.aquatan.plannap.data.model.PostSpot
import java.io.ByteArrayOutputStream

class PlanRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://plannap.aquatan.studio")
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            )
        )
        .build()

    private val service = retrofit.create(PlanService::class.java)


    fun getPlanList(): LiveData<List<Plan>> {
        val result = MutableLiveData<List<Plan>>()

        GlobalScope.launch {
            try {
                val response = service.getPlans().execute()
                result.postValue(response.body())
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

    fun registerPlan(name: String, note: String, duration: Int?, price: Int?, editableSpotList: List<EditableSpot>) =
        GlobalScope.async {
            val spotList = editableSpotList.map { spot ->
                if (spot.picture == null || spot.lat == null || spot.lon == null) {
                    return@async false
                }

                val stream = ByteArrayOutputStream()
                spot.picture.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes = stream.toByteArray()

                return@map PostSpot(
                    spot.name,
                    spot.note,
                    Base64.encodeToString(bytes, Base64.DEFAULT),
                    spot.lat,
                    spot.lon)
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