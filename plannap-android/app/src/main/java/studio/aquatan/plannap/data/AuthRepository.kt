package studio.aquatan.plannap.data

import android.util.Log
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import studio.aquatan.plannap.Session
import studio.aquatan.plannap.data.api.AuthService
import studio.aquatan.plannap.data.model.LoginUser

class AuthRepository(
    private val session: Session
): BaseRepository(session) {

    private val service = baseRetrofit.build().create(AuthService::class.java)

    fun login(username: String, password: String) =
        GlobalScope.async {
            try {
                val user = LoginUser(username, password)

                val response = service.login(user).execute()
                val auth = response.body()

                if (response.isSuccessful && auth != null) {
                    session.username = username
                    session.password = password
                    session.token = auth.token

                    return@async true
                }
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to login", e)
            }
            Log.d(javaClass.simpleName, "Failed to login")

            return@async false
        }
}