package studio.aquatan.plannap.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import studio.aquatan.plannap.data.NetworkState
import studio.aquatan.plannap.data.api.PlanService
import studio.aquatan.plannap.data.model.Page
import studio.aquatan.plannap.data.model.Plan

class PlanDataSource(
    private val service: PlanService
) : PageKeyedDataSource<String, Plan>() {

    companion object {
        private const val TAG = "PlanDataSource"
    }

    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Plan>) {
        networkState.postValue(NetworkState.LOADING)

        try {
            val response = service.getPlans(null).execute()
            val page = response.body() ?: Page()

            networkState.postValue(NetworkState.LOADED)

            callback.onResult(page.resultList, null, page.nextKey)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load plans", e)

            val error = NetworkState.error(e.message ?: "unknown error")
            networkState.postValue(error)
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Plan>) {
        networkState.postValue(NetworkState.LOADING)

        try {
            val response = service.getPlans(params.key).execute()
            val page = response.body() ?: Page()

            networkState.postValue(NetworkState.LOADED)

            callback.onResult(page.resultList, page.nextKey)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load plans", e)

            val error = NetworkState.error(e.message ?: "unknown error")
            networkState.postValue(error)
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Plan>) {}
}