package studio.aquatan.plannap.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import studio.aquatan.plannap.data.NetworkState
import studio.aquatan.plannap.data.api.PlanService
import studio.aquatan.plannap.data.model.Page
import studio.aquatan.plannap.data.model.Plan

class MyPlanDataSource(
    private val service: PlanService
) : PageKeyedDataSource<String, Plan>() {

    companion object {
        private const val TAG = "PlanDataSource"
    }

    private var retry: (() -> Unit)? = null

    val initialLoad = MutableLiveData<NetworkState>()
    val networkState = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            GlobalScope.launch { it.invoke() }
        }
    }

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Plan>) {
        initialLoad.postValue(NetworkState.LOADING)

        try {
            val response = service.getMyPlans(null).execute()
            val page = response.body() ?: Page()

            retry = null
            initialLoad.postValue(NetworkState.LOADED)
            networkState.postValue(NetworkState.LOADED)

            callback.onResult(page.resultList, null, page.nextKey)
        } catch (e: Exception) {
            Log.e(TAG, "loadInitial() failed", e)

            retry = { loadInitial(params, callback) }

            val error = NetworkState.error(e.message ?: "unknown error")
            initialLoad.postValue(error)
            networkState.postValue(error)
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Plan>) {
        networkState.postValue(NetworkState.LOADING)

        try {
            val response = service.getMyPlans(params.key).execute()
            val page = response.body() ?: Page()

            retry = null
            networkState.postValue(NetworkState.LOADED)

            callback.onResult(page.resultList, page.nextKey)
        } catch (e: Exception) {
            Log.e(TAG, "loadAfter() failed", e)

            retry = { loadAfter(params, callback) }

            val error = NetworkState.error(e.message ?: "unknown error")
            networkState.postValue(error)
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Plan>) {}
}