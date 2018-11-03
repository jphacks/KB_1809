package studio.aquatan.plannap.data

import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import studio.aquatan.plannap.Session
import studio.aquatan.plannap.data.adapter.DateJsonAdapter
import studio.aquatan.plannap.data.api.AuthService
import studio.aquatan.plannap.data.model.PostUser

abstract class BaseRepository(
    private val session: Session
) {

    companion object {
        private const val BASE_URL = "https://plannap.aquatan.studio"
    }

    protected val retrofitBuilder: Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(DateJsonAdapter())
                        .build()
                )
            )

    protected fun buildRetrofit(): Retrofit = retrofitBuilder.client(buildClient()).build()

    private fun buildClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(buildAuthInterceptor())
            .build()

    private fun buildAuthInterceptor(): AuthorizationInterceptor {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return AuthorizationInterceptor(retrofit.create(AuthService::class.java))
    }

    private inner class AuthorizationInterceptor(
        private val authService: AuthService
    ) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            var response = chain.proceed(
                request.newBuilder()
                    .addHeader("Authorization", "JWT ${session.token}")
                    .build()
            )

            // Require auth
            if (response.code() == 401 || response.code() == 403) {
                val user = PostUser(session.username, session.password)

                val loginResponse = authService.login(user).execute()
                val auth = loginResponse.body()

                if (loginResponse.isSuccessful && auth != null) {
                    session.token = auth.token

                    val builder = request.newBuilder()
                        .header("Authorization", "JWT ${session.token}")

                    response = chain.proceed(builder.build())
                }
            }

            return response
        }
    }
}