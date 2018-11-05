package studio.aquatan.plannap.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import studio.aquatan.plannap.Session
import studio.aquatan.plannap.data.api.UserService
import studio.aquatan.plannap.data.model.User

class UserRepository(session: Session) : BaseRepository(session) {

    companion object {
        private const val TAG = "UserRepository"
    }

    private val service = buildRetrofit().create(UserService::class.java)

    fun getUser(): LiveData<User> {
        val result = MutableLiveData<User>()

        GlobalScope.launch {
            try {
                val response = service.getUser().execute()
                result.postValue(response.body())
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch getUser", e)
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