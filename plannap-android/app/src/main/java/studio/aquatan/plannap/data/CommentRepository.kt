package studio.aquatan.plannap.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import studio.aquatan.plannap.data.api.CommentService
import studio.aquatan.plannap.data.api.PlanService
import studio.aquatan.plannap.data.model.Comment
import studio.aquatan.plannap.data.model.Plan

class CommentRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://plannap.aquatan.studio")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = retrofit.create(CommentService::class.java)

    fun getCommentList(): LiveData<List<Comment>> {
        val result = MutableLiveData<List<Comment>>()

        GlobalScope.launch {
            // TODO fetch plan list via API
            delay(2000)
            result.postValue(DUMMY_LIST)

//            try {
//                val response = service.plans().execute()
//                result.postValue(response.body())
//            } catch (e: Exception) {
//                Log.e(javaClass.simpleName, "Failed to fetch plans", e)
//            }
        }

        return result
    }

    fun getCommentById(id: Long): LiveData<Comment> {
        val result = MutableLiveData<Comment>()

        GlobalScope.launch {
            // TODO fetch plan via API
            delay(1000)
            result.postValue(DUMMY_LIST.find { it.id == id })
        }

        return result
    }
}