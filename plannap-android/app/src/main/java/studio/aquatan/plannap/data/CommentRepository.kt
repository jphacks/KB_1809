package studio.aquatan.plannap.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import studio.aquatan.plannap.Session
import studio.aquatan.plannap.data.api.CommentService
import studio.aquatan.plannap.data.model.Comment
import studio.aquatan.plannap.data.model.PostComment

class CommentRepository(session: Session): BaseRepository(session) {

    private val service = buildRetrofit().create(CommentService::class.java)

    fun getCommentList(planId: Long): LiveData<List<Comment>> {
        val result = MutableLiveData<List<Comment>>()

        GlobalScope.launch {
            try {
                val response = service.getComments(planId).execute()
                result.postValue(response.body() ?: emptyList())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch getComments", e)
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