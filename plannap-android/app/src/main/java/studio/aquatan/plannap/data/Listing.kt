package studio.aquatan.plannap.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val initialLoad: LiveData<NetworkState>,
    val networkState: LiveData<NetworkState>,
    val retry: () -> Unit,
    val refresh: () -> Unit
)