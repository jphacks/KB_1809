package studio.aquatan.plannap.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import studio.aquatan.plannap.data.api.UserService
import studio.aquatan.plannap.data.model.User

class UserRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://plannap.aquatan.studio")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = retrofit.create(UserService::class.java)

    fun getUserById(userId: Long): LiveData<User> {
        val result = MutableLiveData<User>()

        GlobalScope.launch {
            try {
                val response = service.getUser(userId).execute()
                result.postValue(response.body())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch getUser", e)
            }
        }

        return result
    }

    fun registerUser(targetUser: User) {
        GlobalScope.launch {
            try {
                service.postUser(targetUser).execute()
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch postUser", e)
            }
        }
    }

}