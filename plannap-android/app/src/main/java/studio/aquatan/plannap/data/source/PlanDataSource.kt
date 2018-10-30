package studio.aquatan.plannap.data.source

import android.util.Log
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import studio.aquatan.plannap.data.api.PlanService
import studio.aquatan.plannap.data.model.Plan

class PlanDataSource(
    private val service: PlanService
) : PageKeyedDataSource<Int, Plan>() {

    companion object {
        private const val TAG = "PlanDataSource"
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Plan>) {
        GlobalScope.launch {
            try {
                val response = service.getPlans(0).execute()
                callback.onResult(response.body() ?: emptyList(), null, 1)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch plans", e)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Plan>) {
        GlobalScope.launch {
            try {
                val response = service.getPlans(params.key).execute()
                callback.onResult(response.body() ?: emptyList(), params.key + 1)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch plans", e)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Plan>) {}
}