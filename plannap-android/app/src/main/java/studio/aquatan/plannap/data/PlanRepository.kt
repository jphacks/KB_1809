package studio.aquatan.plannap.data

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Moshi
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import studio.aquatan.plannap.Session
import studio.aquatan.plannap.data.api.PlanService
import studio.aquatan.plannap.data.model.*
import studio.aquatan.plannap.util.mapParallel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class PlanRepository(context: Context, session: Session): BaseRepository(session) {
    
    companion object {
        private const val TAG = "PlanRepository"

        const val OUTPUT_PATH = "post-plan-outputs"
    }

    private val service = buildRetrofit().create(PlanService::class.java)
    private val filesDir = context.filesDir
    private val postPlanJsonAdapter: PostPlanJsonAdapter by lazy {
        val moshi = Moshi.Builder()
            .build()

        return@lazy PostPlanJsonAdapter(moshi)
    }

    fun getPlanList(): LiveData<List<Plan>> {
        val result = MutableLiveData<List<Plan>>()

        GlobalScope.launch {
            try {
                val response = service.getPlans().execute()
                result.postValue(response.body() ?: emptyList())
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch plans", e)
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
                Log.e(TAG, "Failed to fetch getPlan", e)
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
                Log.e(TAG, "Failed to fetch getPlan", e)
            }
        }

        return result
    }

    fun postPlan(postPlan: PostPlan) =
        GlobalScope.async {
            try {
                val response = service.postPlan(postPlan).execute()
                Log.d(TAG, "response code: ${response.code()}")

                return@async response.code() == 201
            } catch (e: Exception) {
                Log.e(TAG, "Failed to post a Plan", e)
            }

            return@async false
        }

    fun savePlanToFile(name: String, note: String, duration: Int, price: Int, editableSpotList: List<EditableSpot>) =
        GlobalScope.async {
            val postSpotList = editableSpotList.asPostSpotList()
            val postPlan = PostPlan(name, price, duration, note, postSpotList)
            
            val uuid = UUID.randomUUID()

            val fileName = "$uuid.json"
            val outputDir = File(filesDir, OUTPUT_PATH)
            if (!outputDir.exists()) {
                outputDir.mkdirs() // should succeed
            }

            val outputFile = File(outputDir, fileName)
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(outputFile)
                out.write(postPlanJsonAdapter.toJson(postPlan).toByteArray())
            } catch (e: Exception) {
                Log.e(TAG, "Failed to output", e)
            } finally {
                out?.close()
            }

            return@async uuid
        }

    private fun List<EditableSpot>.asPostSpotList() =
        mapParallel {
            val image = it.image ?: throw NullPointerException("EditableSpot image is null")

            val out = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 80, out)
            val bytes = out.toByteArray()
            out.close()

            return@mapParallel PostSpot(
                it.name,
                it.note,
                Base64.encodeToString(bytes, Base64.DEFAULT),
                it.lat,
                it.long
            )
        }
}