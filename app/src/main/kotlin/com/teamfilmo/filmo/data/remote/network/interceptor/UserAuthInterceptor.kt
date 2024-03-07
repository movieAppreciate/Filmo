package com.teamfilmo.filmo.data.remote.network.interceptor

import com.teamfilmo.filmo.data.local.source.UserTokenSource
import com.teamfilmo.filmo.data.remote.service.AuthService
import java.net.HttpURLConnection
import javax.inject.Provider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class UserAuthInterceptor(
    private val dataStore: Provider<UserTokenSource>,
    private val authService: Provider<AuthService>,
) : Interceptor {
    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response =
        runBlocking {
            val request = chain.request()

            // No AUTH 의 경우 token을 넣지 않는다.
            if ("true" in request.headers.values("NO-AUTH")) {
                return@runBlocking chain.proceed(request)
            }

            val accessToken =
                dataStore.get().getUserToken().firstOrNull()?.let { it.ifEmpty { null } }

            val response = chain.proceedWithToken(request, accessToken)

            // 정상 응답인 경우 그대로 return || accessToken이 없는 경우(Logout, 처음 사용자), 그대로 return
            if (response.code != HttpURLConnection.HTTP_UNAUTHORIZED || accessToken == null) {
                return@runBlocking response
            }

            val (newAccessToken, newRefreshToken) =
                mutex.withLock {
                    val currentAccessToken = dataStore.get().getUserToken().firstOrNull()
                    val currentRefreshToken = dataStore.get().getRefreshToken().firstOrNull()

                    if (currentAccessToken.isNullOrBlank() || currentRefreshToken.isNullOrBlank()) {
                        return@withLock Token(null, null)
                    }

                    val result = authService.get().refreshToken(currentAccessToken, currentRefreshToken)

                    return@withLock result.map { Token(it, it) }
                        .fold(
                            onSuccess = { it },
                            onFailure = { Token(null, null) },
                        )
                }

            return@runBlocking if (newAccessToken != null && newRefreshToken != null) {
                dataStore.get().updateToken(newAccessToken, newRefreshToken)
                chain.proceedWithToken(request, newAccessToken)
            } else {
                dataStore.get().clearUserToken()
                response
            }
        }

    private fun Interceptor.Chain.proceedWithToken(
        request: Request,
        token: String?,
    ): Response {
        return request
            .newBuilder()
            .apply {
                if (token.isNullOrBlank().not()) {
                    addHeader(AUTHORIZATION, "Bearer $token")
                }
            }
            .build()
            .let(::proceed)
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }

    data class Token(
        val accessToken: String?,
        val refreshToken: String?,
    )
}
