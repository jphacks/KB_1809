package studio.aquatan.plannap.data

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import studio.aquatan.plannap.data.api.PlanService
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.data.model.PostPlan
import studio.aquatan.plannap.data.model.PostSpotMeta
import studio.aquatan.plannap.ui.plan.post.PostSpot
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

    fun registerPlan(name: String, price: Int, duration: Int, note: String, postSpotList: List<PostSpot>) {

        val metaList: List<PostSpotMeta> = postSpotList.map {
            val mipmap = it.picture ?: return
            val byteArrayOutputStream = ByteArrayOutputStream()
            mipmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()

            return@map PostSpotMeta(
                it.name ?: "unknown",
                it.note ?: "nothing",
                Base64.encodeToString(byteArray, Base64.DEFAULT),
//                it.latLong?.get(0) ?: 135.0f,
//                it.latLong?.get(1) ?: 35.0f
            135.0f,
                35.0f
            )
        }

//        val moshi = Moshi.Builder().build()
//        val jsonAdapter = moshi.adapter(PostPlan::class.java)
        val plan = PostPlan(name, price, duration, note, metaList)
//        val postTarget: HashMap<String, Any> = hashMapOf(
//            "name" to name, "price" to price, "duration" to duration,
//            "note" to note, "spots" to metaList
//        )

        GlobalScope.launch {
            try {
//                val json_str = jsonAdapter.toJson(plan)
//                val json = JSONObject(json_str)
                service.postPlan(plan).execute()
//                Log.d("test-api",json.toString())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch postPlan", e)
            }
        }
    }
}