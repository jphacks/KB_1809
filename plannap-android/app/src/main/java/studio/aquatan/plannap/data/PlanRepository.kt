package studio.aquatan.plannap.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.squareup.moshi.Moshi
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import studio.aquatan.plannap.Session
import studio.aquatan.plannap.data.adapter.UriJsonAdapter
import studio.aquatan.plannap.data.api.PlanService
import studio.aquatan.plannap.data.model.EditablePlan
import studio.aquatan.plannap.data.model.EditablePlanJsonAdapter
import studio.aquatan.plannap.data.model.EditableSpot
import studio.aquatan.plannap.data.model.Plan
import studio.aquatan.plannap.data.model.PostPlan
import studio.aquatan.plannap.data.source.PlanDataSourceFactory
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PlanRepository(
    context: Context,
    session: Session
) : BaseRepository(session) {

    companion object {
        private const val TAG = "PlanRepository"

        const val OUTPUT_PATH = "editable-plan-outputs"
    }

    private val service = buildRetrofit().create(PlanService::class.java)
    private val filesDir = context.filesDir
    private val editablePlanJsonAdapter: EditablePlanJsonAdapter by lazy {
        val moshi = Moshi.Builder()
            .add(UriJsonAdapter())
            .build()

        return@lazy EditablePlanJsonAdapter(moshi)
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
            val plan = EditablePlan(name, note, duration, price, editableSpotList)
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
                out.write(editablePlanJsonAdapter.toJson(plan).toByteArray())
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save", e)
            } finally {
                out?.close()
            }

            return@async uuid
        }

    fun getListing(): Listing<Plan> {
        val factory = PlanDataSourceFactory(service)
        val livePagedList = factory.toLiveData(pageSize = 5)

        return Listing(
            pagedList = livePagedList,
            initialLoad = Transformations.switchMap(factory.sourceLiveData) { it.initialLoad },
            networkState = Transformations.switchMap(factory.sourceLiveData) { it.networkState },
            retry = { factory.sourceLiveData.value?.retryAllFailed() },
            refresh = { factory.sourceLiveData.value?.invalidate() }
        )
    }
}