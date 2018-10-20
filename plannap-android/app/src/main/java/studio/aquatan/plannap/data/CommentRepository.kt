package studio.aquatan.plannap.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import studio.aquatan.plannap.data.api.CommentService
import studio.aquatan.plannap.data.model.Comment

class CommentRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://plannap.aquatan.studio")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = retrofit.create(CommentService::class.java)

    fun getCommentList(planId: Long): LiveData<List<Comment>> {
        val result = MutableLiveData<List<Comment>>()

        GlobalScope.launch {
            try {
                val response = service.getComments(planId).execute()
                result.postValue(response.body())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch getComments", e)
            }
        }

        return result
    }

    fun registerSpot(targetComment: Comment) {
        GlobalScope.launch {
            try {
                service.postComment(targetComment).execute()
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch postComment", e)
            }
        }
    }
}