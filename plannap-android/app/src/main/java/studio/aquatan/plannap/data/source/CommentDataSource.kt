package studio.aquatan.plannap.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import studio.aquatan.plannap.data.NetworkState
import studio.aquatan.plannap.data.api.CommentService
import studio.aquatan.plannap.data.model.Comment
import studio.aquatan.plannap.data.model.Page

class CommentDataSource(
    private val service: CommentService,
    private val planId: Long
) : PageKeyedDataSource<String, Comment>() {

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

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Comment>) {
        initialLoad.postValue(NetworkState.LOADING)

        try {
            val response = service.getComments(planId, null).execute()
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

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Comment>) {
        networkState.postValue(NetworkState.LOADING)

        try {
            val response = service.getComments(planId, params.key).execute()
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

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Comment>) {}
}