package studio.aquatan.plannap.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import studio.aquatan.plannap.Session
import studio.aquatan.plannap.data.api.CommentService
import studio.aquatan.plannap.data.model.Comment
import studio.aquatan.plannap.data.model.PostComment
import studio.aquatan.plannap.data.source.CommentDataSourceFactory

class CommentRepository(session: Session) : BaseRepository(session) {

    companion object {
        const val PAGE_SIZE = 10
    }

    private val service = buildRetrofit().create(CommentService::class.java)

    fun getCommentListing(planId: Long): Listing<Comment> {
        val factory = CommentDataSourceFactory(service, planId)
        val livePagedList = factory.toLiveData(pageSize = PAGE_SIZE)

        return Listing(
            pagedList = livePagedList,
            initialLoad = Transformations.switchMap(factory.sourceLiveData) { it.initialLoad },
            networkState = Transformations.switchMap(factory.sourceLiveData) { it.networkState },
            retry = { factory.sourceLiveData.value?.retryAllFailed() },
            refresh = { factory.sourceLiveData.value?.invalidate() }
        )
    }

    fun getNewestCommentList(planId: Long, limit: Int): LiveData<List<Comment>> {
        val result = MutableLiveData<List<Comment>>()

        GlobalScope.launch {
            try {
                val response = service.getComments(planId, null, limit).execute()
                val list = response.body()?.resultList ?: emptyList()

                result.postValue(list.sortedBy { it.createdDate.time })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return result
    }

    fun postComment(planId: Long, text: String) =
        GlobalScope.async {
            try {
                val response = service.postComment(planId, PostComment(text)).execute()
                return@async Pair(response.code() == 201, response.message())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to post a Comment", e)
            }

            return@async Pair(false, "Unknown error")
        }
}