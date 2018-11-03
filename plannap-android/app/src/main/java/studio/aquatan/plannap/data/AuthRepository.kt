package studio.aquatan.plannap.data

import android.util.Log
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import studio.aquatan.plannap.Session
import studio.aquatan.plannap.data.api.AuthService
import studio.aquatan.plannap.data.model.Authorization
import studio.aquatan.plannap.data.model.PostUser

class AuthRepository(
    private val session: Session
) : BaseRepository(session) {

    companion object {
        private const val TAG = "AuthRepository"
    }

    private val service = buildRetrofit().create(AuthService::class.java)
    private val loginService = retrofitBuilder.build().create(AuthService::class.java)

    fun login(username: String, password: String) =
        GlobalScope.async {
            try {
                val user = PostUser(username, password)

                val response = loginService.login(user).execute()
                val auth = response.body()

                if (response.isSuccessful && auth != null) {
                    session.username = username
                    session.password = password
                    session.token = auth.token

                    return@async true
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to login", e)
            }
            Log.d(TAG, "Failed to login")

            return@async false
        }

    fun verify() = GlobalScope.async {
        try {
            val response = service.verify(Authorization(session.token)).execute()
            val auth = response.body()

            if (response.isSuccessful && auth != null) {
                session.token = auth.token
                return@async true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to verify", e)
        }
        Log.d(TAG, "Failed to verify")

        return@async false
    }
}